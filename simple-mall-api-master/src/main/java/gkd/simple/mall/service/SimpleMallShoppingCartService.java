
package gkd.simple.mall.service;

import gkd.simple.mall.api.mall.param.SaveCartItemParam;
import gkd.simple.mall.api.mall.param.UpdateCartItemParam;
import gkd.simple.mall.api.mall.vo.SimpleMallShoppingCartItemVO;
import gkd.simple.mall.entity.SimpleMallShoppingCartItem;
import gkd.simple.mall.util.PageQueryUtil;
import gkd.simple.mall.util.PageResult;

import java.util.List;

public interface SimpleMallShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param saveCartItemParam
     * @param userId
     * @return
     */
    String saveSimpleMallCartItem(SaveCartItemParam saveCartItemParam, Long userId);

    /**
     * 修改购物车中的属性
     *
     * @param updateCartItemParam
     * @param userId
     * @return
     */
    String updateSimpleMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

    /**
     * 获取购物项详情
     *
     * @param simpleMallShoppingCartItemId
     * @return
     */
    SimpleMallShoppingCartItem getSimpleMallCartItemById(Long simpleMallShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param simpleMallShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long simpleMallShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param simpleMallUserId
     * @return
     */
    List<SimpleMallShoppingCartItemVO> getMyShoppingCartItems(Long simpleMallUserId);

    /**
     * 根据userId和cartItemIds获取对应的购物项记录
     *
     * @param cartItemIds
     * @param simpleMallUserId
     * @return
     */
    List<SimpleMallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long simpleMallUserId);

    /**
     * 我的购物车(分页数据)
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyShoppingCartItems(PageQueryUtil pageUtil);
}
