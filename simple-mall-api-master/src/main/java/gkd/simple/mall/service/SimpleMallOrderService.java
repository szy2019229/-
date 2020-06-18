
package gkd.simple.mall.service;

import gkd.simple.mall.api.mall.vo.SimpleMallOrderDetailVO;
import gkd.simple.mall.api.mall.vo.SimpleMallShoppingCartItemVO;
import gkd.simple.mall.entity.MallUser;
import gkd.simple.mall.entity.MallUserAddress;
import gkd.simple.mall.entity.SimpleMallOrder;
import gkd.simple.mall.entity.SimpleMallOrderItem;
import gkd.simple.mall.util.PageQueryUtil;
import gkd.simple.mall.util.PageResult;

import java.util.List;

public interface SimpleMallOrderService {

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getSimpleMallOrdersPage(PageQueryUtil pageUtil);

    List<SimpleMallOrder> getOrders();
    List<SimpleMallOrderItem> getOrderItems(Long id);

    /**
     * 订单信息修改
     *
     * @param simpleMallOrder
     * @return
     */
    String updateOrderInfo(SimpleMallOrder simpleMallOrder);
    Boolean deleteById(Long id);
    Boolean deleteBatch(Long ids[]);
    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);



    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    SimpleMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    SimpleMallOrder getOrderByOrderId(Long OrderId);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    String saveOrder(MallUser loginMallUser, MallUserAddress address, List<SimpleMallShoppingCartItemVO> itemsForSave);
}
