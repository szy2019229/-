
package gkd.simple.mall.dao;

import gkd.simple.mall.entity.SimpleMallShoppingCartItem;
import gkd.simple.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SimpleMallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(SimpleMallShoppingCartItem record);

    int insertSelective(SimpleMallShoppingCartItem record);

    SimpleMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    SimpleMallShoppingCartItem selectByUserIdAndGoodsId(@Param("simpleMallUserId") Long simpleMallUserId, @Param("goodsId") Long goodsId);

    List<SimpleMallShoppingCartItem> selectByUserId(@Param("simpleMallUserId") Long simpleMallUserId, @Param("number") int number);

    List<SimpleMallShoppingCartItem> selectByUserIdAndCartItemIds(@Param("simpleMallUserId") Long simpleMallUserId, @Param("cartItemIds") List<Long> cartItemIds);

    int selectCountByUserId(Long simpleMallUserId);

    int updateByPrimaryKeySelective(SimpleMallShoppingCartItem record);

    int updateByPrimaryKey(SimpleMallShoppingCartItem record);

    int deleteBatch(List<Long> ids);

    List<SimpleMallShoppingCartItem> findMySimpleMallCartItems(PageQueryUtil pageUtil);

    int getTotalMySimpleMallCartItems(PageQueryUtil pageUtil);
}