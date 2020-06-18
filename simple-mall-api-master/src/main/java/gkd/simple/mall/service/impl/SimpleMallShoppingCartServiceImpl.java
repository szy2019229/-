
package gkd.simple.mall.service.impl;

import gkd.simple.mall.api.mall.param.SaveCartItemParam;
import gkd.simple.mall.api.mall.param.UpdateCartItemParam;
import gkd.simple.mall.common.Constants;
import gkd.simple.mall.common.SimpleMallException;
import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.api.mall.vo.SimpleMallShoppingCartItemVO;
import gkd.simple.mall.dao.SimpleMallGoodsMapper;
import gkd.simple.mall.dao.SimpleMallShoppingCartItemMapper;
import gkd.simple.mall.entity.SimpleMallGoods;
import gkd.simple.mall.entity.SimpleMallShoppingCartItem;
import gkd.simple.mall.service.SimpleMallShoppingCartService;
import gkd.simple.mall.util.BeanUtil;
import gkd.simple.mall.util.PageQueryUtil;
import gkd.simple.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SimpleMallShoppingCartServiceImpl implements SimpleMallShoppingCartService {

    @Autowired
    private SimpleMallShoppingCartItemMapper simpleMallShoppingCartItemMapper;

    @Autowired
    private SimpleMallGoodsMapper simpleMallGoodsMapper;

    @Override
    public String saveSimpleMallCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
        SimpleMallShoppingCartItem temp = simpleMallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, saveCartItemParam.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            SimpleMallException.fail(ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult());
        }
        SimpleMallGoods simpleMallGoods = simpleMallGoodsMapper.selectByPrimaryKey(saveCartItemParam.getGoodsId());
        //商品为空
        if (simpleMallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = simpleMallShoppingCartItemMapper.selectCountByUserId(userId);
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        SimpleMallShoppingCartItem simpleMallShoppingCartItem = new SimpleMallShoppingCartItem();
        BeanUtil.copyProperties(saveCartItemParam, simpleMallShoppingCartItem);
        simpleMallShoppingCartItem.setUserId(userId);
        //保存记录
        if (simpleMallShoppingCartItemMapper.insertSelective(simpleMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateSimpleMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
        SimpleMallShoppingCartItem simpleMallShoppingCartItemUpdate = simpleMallShoppingCartItemMapper.selectByPrimaryKey(updateCartItemParam.getCartItemId());
        if (simpleMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!simpleMallShoppingCartItemUpdate.getUserId().equals(userId)) {
            SimpleMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        //超出单个商品的最大数量
        if (updateCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        simpleMallShoppingCartItemUpdate.setGoodsCount(updateCartItemParam.getGoodsCount());
        simpleMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (simpleMallShoppingCartItemMapper.updateByPrimaryKeySelective(simpleMallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public SimpleMallShoppingCartItem getSimpleMallCartItemById(Long simpleMallShoppingCartItemId) {
        SimpleMallShoppingCartItem simpleMallShoppingCartItem = simpleMallShoppingCartItemMapper.selectByPrimaryKey(simpleMallShoppingCartItemId);
        if (simpleMallShoppingCartItem == null) {
            SimpleMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return simpleMallShoppingCartItem;
    }

    @Override
    public Boolean deleteById(Long simpleMallShoppingCartItemId) {
        return simpleMallShoppingCartItemMapper.deleteByPrimaryKey(simpleMallShoppingCartItemId) > 0;
    }

    @Override
    public List<SimpleMallShoppingCartItemVO> getMyShoppingCartItems(Long simpleMallUserId) {
        List<SimpleMallShoppingCartItemVO> simpleMallShoppingCartItemVOS = new ArrayList<>();
        List<SimpleMallShoppingCartItem> simpleMallShoppingCartItems = simpleMallShoppingCartItemMapper.selectByUserId(simpleMallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        return getSimpleMallShoppingCartItemVOS(simpleMallShoppingCartItemVOS, simpleMallShoppingCartItems);
    }

    @Override
    public List<SimpleMallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long simpleMallUserId) {
        List<SimpleMallShoppingCartItemVO> simpleMallShoppingCartItemVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(cartItemIds)) {
            SimpleMallException.fail("购物项不能为空");
        }
        List<SimpleMallShoppingCartItem> simpleMallShoppingCartItems = simpleMallShoppingCartItemMapper.selectByUserIdAndCartItemIds(simpleMallUserId, cartItemIds);
        if (CollectionUtils.isEmpty(simpleMallShoppingCartItems)) {
            SimpleMallException.fail("购物项不能为空");
        }
        if (simpleMallShoppingCartItems.size() != cartItemIds.size()) {
            SimpleMallException.fail("参数异常");
        }
        return getSimpleMallShoppingCartItemVOS(simpleMallShoppingCartItemVOS, simpleMallShoppingCartItems);
    }

    /**
     * 数据转换
     *
     * @param simpleMallShoppingCartItemVOS
     * @param simpleMallShoppingCartItems
     * @return
     */
    private List<SimpleMallShoppingCartItemVO> getSimpleMallShoppingCartItemVOS(List<SimpleMallShoppingCartItemVO> simpleMallShoppingCartItemVOS, List<SimpleMallShoppingCartItem> simpleMallShoppingCartItems) {
        if (!CollectionUtils.isEmpty(simpleMallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> simpleMallGoodsIds = simpleMallShoppingCartItems.stream().map(SimpleMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<SimpleMallGoods> simpleMallGoods = simpleMallGoodsMapper.selectByPrimaryKeys(simpleMallGoodsIds);
            Map<Long, SimpleMallGoods> simpleMallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(simpleMallGoods)) {
                simpleMallGoodsMap = simpleMallGoods.stream().collect(Collectors.toMap(SimpleMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (SimpleMallShoppingCartItem simpleMallShoppingCartItem : simpleMallShoppingCartItems) {
                SimpleMallShoppingCartItemVO simpleMallShoppingCartItemVO = new SimpleMallShoppingCartItemVO();
                BeanUtil.copyProperties(simpleMallShoppingCartItem, simpleMallShoppingCartItemVO);
                if (simpleMallGoodsMap.containsKey(simpleMallShoppingCartItem.getGoodsId())) {
                    SimpleMallGoods simpleMallGoodsTemp = simpleMallGoodsMap.get(simpleMallShoppingCartItem.getGoodsId());
                    simpleMallShoppingCartItemVO.setGoodsCoverImg(simpleMallGoodsTemp.getGoodsCoverImg());
                    String goodsName = simpleMallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    simpleMallShoppingCartItemVO.setGoodsName(goodsName);
                    simpleMallShoppingCartItemVO.setSellingPrice(simpleMallGoodsTemp.getSellingPrice());
                    simpleMallShoppingCartItemVOS.add(simpleMallShoppingCartItemVO);
                }
            }
        }
        return simpleMallShoppingCartItemVOS;
    }

    @Override
    public PageResult getMyShoppingCartItems(PageQueryUtil pageUtil) {
        List<SimpleMallShoppingCartItemVO> simpleMallShoppingCartItemVOS = new ArrayList<>();
        List<SimpleMallShoppingCartItem> simpleMallShoppingCartItems = simpleMallShoppingCartItemMapper.findMySimpleMallCartItems(pageUtil);
        int total = simpleMallShoppingCartItemMapper.getTotalMySimpleMallCartItems(pageUtil);
        PageResult pageResult = new PageResult(getSimpleMallShoppingCartItemVOS(simpleMallShoppingCartItemVOS, simpleMallShoppingCartItems), total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
