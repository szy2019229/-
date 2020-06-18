
package gkd.simple.mall.service.impl;

import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.dao.SimpleMallGoodsMapper;
import gkd.simple.mall.entity.SimpleMallGoods;
import gkd.simple.mall.service.SimpleMallGoodsService;
import gkd.simple.mall.util.BeanUtil;
import gkd.simple.mall.util.PageQueryUtil;
import gkd.simple.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SimpleMallGoodsServiceImpl implements SimpleMallGoodsService {

    @Autowired
    private SimpleMallGoodsMapper goodsMapper;

    @Override
    public PageResult getSimpleMallGoodsPage(PageQueryUtil pageUtil) {
        List<SimpleMallGoods> goodsList = goodsMapper.findSimpleMallGoodsList(pageUtil);
        int total = goodsMapper.getTotalSimpleMallGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveSimpleMallGoods(SimpleMallGoods goods) {
        goods.setOriginalPrice(Integer.valueOf(goods.getSellingPrice()));
        goods.setSellingPrice(Integer.valueOf(goods.getOriginalPrice()));
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveSimpleMallGoods(List<SimpleMallGoods> simpleMallGoodsList) {
        if (!CollectionUtils.isEmpty(simpleMallGoodsList)) {
            goodsMapper.batchInsert(simpleMallGoodsList);
        }
    }

    @Override
    public Boolean deleteById(Long id){
        return goodsMapper.deleteByPrimaryKey(id) > 0;
    }
    @Override
    public Boolean deleteBatch(Integer id[]){
        if(id.length < 1)
            return false;
        return goodsMapper.deleteBatch(id) > 0;
    }

    @Override
    public String updateSimpleMallGoods(SimpleMallGoods goods) {
        SimpleMallGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public SimpleMallGoods getSimpleMallGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    @Override
    public PageResult searchSimpleMallGoods(PageQueryUtil pageUtil) {
        List<SimpleMallGoods> goodsList = goodsMapper.findSimpleMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalSimpleMallGoodsBySearch(pageUtil);
        List<SimpleMallGoods> simpleMallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            simpleMallSearchGoodsVOS = BeanUtil.copyList(goodsList, SimpleMallGoods.class);
            for (SimpleMallGoods simpleMallSearchGoodsVO : simpleMallSearchGoodsVOS) {
                String goodsName = simpleMallSearchGoodsVO.getGoodsName();
                String goodsIntro = simpleMallSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    simpleMallSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    simpleMallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(simpleMallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public List<SimpleMallGoods> getGoods() {
        List<SimpleMallGoods> mallGoodss = goodsMapper.getGoods();
        return mallGoodss;
    }

    @Override
    public PageResult getGoods(PageQueryUtil pageUtil) {
        List<SimpleMallGoods> mallGoodss = goodsMapper.findSimpleMallGoodsList(pageUtil);
        int total = goodsMapper.getTotalSimpleMallGoods(pageUtil);
        PageResult pageResult = new PageResult(mallGoodss, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
