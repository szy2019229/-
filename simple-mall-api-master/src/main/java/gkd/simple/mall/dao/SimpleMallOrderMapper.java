
package gkd.simple.mall.dao;

import gkd.simple.mall.entity.SimpleMallOrder;
import gkd.simple.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SimpleMallOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int deleteBatch(Long ids[]);

    int insert(SimpleMallOrder record);

    int insertSelective(SimpleMallOrder record);

    SimpleMallOrder selectByPrimaryKey(Long orderId);

    SimpleMallOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(SimpleMallOrder record);

    int updateByPrimaryKey(SimpleMallOrder record);

    List<SimpleMallOrder> findSimpleMallOrderList(PageQueryUtil pageUtil);

    List <SimpleMallOrder> getOrders();

    int getTotalSimpleMallOrders(PageQueryUtil pageUtil);

    List<SimpleMallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}