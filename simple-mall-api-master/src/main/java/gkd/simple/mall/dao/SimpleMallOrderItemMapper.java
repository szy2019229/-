
package gkd.simple.mall.dao;

import gkd.simple.mall.entity.SimpleMallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SimpleMallOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(SimpleMallOrderItem record);

    int insertSelective(SimpleMallOrderItem record);

    SimpleMallOrderItem selectByPrimaryKey(Long orderItemId);
    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<SimpleMallOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<SimpleMallOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<SimpleMallOrderItem> orderItems);

    int updateByPrimaryKeySelective(SimpleMallOrderItem record);

    int updateByPrimaryKey(SimpleMallOrderItem record);
}