
package gkd.simple.mall.service.impl;

import gkd.simple.mall.api.mall.vo.SimpleMallOrderDetailVO;
import gkd.simple.mall.api.mall.vo.SimpleMallOrderItemVO;
import gkd.simple.mall.api.mall.vo.SimpleMallOrderListVO;
import gkd.simple.mall.api.mall.vo.SimpleMallShoppingCartItemVO;
import gkd.simple.mall.common.*;
import gkd.simple.mall.dao.*;
import gkd.simple.mall.entity.*;
import gkd.simple.mall.service.SimpleMallOrderService;
import gkd.simple.mall.util.BeanUtil;
import gkd.simple.mall.util.NumberUtil;
import gkd.simple.mall.util.PageQueryUtil;
import gkd.simple.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class SimpleMallOrderServiceImpl implements SimpleMallOrderService {

    @Autowired
    private SimpleMallOrderMapper simpleMallOrderMapper;
    @Autowired
    private SimpleMallOrderItemMapper simpleMallOrderItemMapper;
    @Autowired
    private SimpleMallShoppingCartItemMapper simpleMallShoppingCartItemMapper;
    @Autowired
    private SimpleMallGoodsMapper simpleMallGoodsMapper;
    @Autowired
    private SimpleMallOrderAddressMapper simpleMallOrderAddressMapper;

    @Override
    public List<SimpleMallOrder> getOrders(){
        return simpleMallOrderMapper.getOrders();
    }

    @Override
    public SimpleMallOrder getOrderByOrderId(Long OrderId){
        return simpleMallOrderMapper.selectByPrimaryKey(OrderId);
    }
    @Override
    public PageResult getSimpleMallOrdersPage(PageQueryUtil pageUtil) {
        List<SimpleMallOrder> simpleMallOrders = simpleMallOrderMapper.findSimpleMallOrderList(pageUtil);
        int total = simpleMallOrderMapper.getTotalSimpleMallOrders(pageUtil);
        PageResult pageResult = new PageResult(simpleMallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(SimpleMallOrder simpleMallOrder) {
        SimpleMallOrder temp = simpleMallOrderMapper.selectByPrimaryKey(simpleMallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(simpleMallOrder.getTotalPrice());
            temp.setUpdateTime(new Date());
            if (simpleMallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SimpleMallOrder> orders = simpleMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SimpleMallOrder simpleMallOrder : orders) {
                if (simpleMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += simpleMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (simpleMallOrder.getOrderStatus() != 1) {
                    errorOrderNos += simpleMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (simpleMallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SimpleMallOrder> orders = simpleMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SimpleMallOrder simpleMallOrder : orders) {
                if (simpleMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += simpleMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (simpleMallOrder.getOrderStatus() != 1 && simpleMallOrder.getOrderStatus() != 2) {
                    errorOrderNos += simpleMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (simpleMallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SimpleMallOrder> orders = simpleMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SimpleMallOrder simpleMallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (simpleMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += simpleMallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (simpleMallOrder.getOrderStatus() == 4 || simpleMallOrder.getOrderStatus() < 0) {
                    errorOrderNos += simpleMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (simpleMallOrderMapper.closeOrder(Arrays.asList(ids), SimpleMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }
    @Override
    public List<SimpleMallOrderItem> getOrderItems(Long id) {
        SimpleMallOrder simpleMallOrder = simpleMallOrderMapper.selectByPrimaryKey(id);
        if (simpleMallOrder != null) {
            List<SimpleMallOrderItem> orderItems = simpleMallOrderItemMapper.selectByOrderId(simpleMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                return orderItems;
            }
        }
        return null;
    }







    @Override
    public SimpleMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        SimpleMallOrder simpleMallOrder = simpleMallOrderMapper.selectByOrderNo(orderNo);
        if (simpleMallOrder == null) {
            SimpleMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (!userId.equals(simpleMallOrder.getUserId())) {
            SimpleMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        List<SimpleMallOrderItem> orderItems = simpleMallOrderItemMapper.selectByOrderId(simpleMallOrder.getOrderId());
        //获取订单项数据
        if (!CollectionUtils.isEmpty(orderItems)) {
            List<SimpleMallOrderItemVO> simpleMallOrderItemVOS = BeanUtil.copyList(orderItems, SimpleMallOrderItemVO.class);
            SimpleMallOrderDetailVO simpleMallOrderDetailVO = new SimpleMallOrderDetailVO();
            BeanUtil.copyProperties(simpleMallOrder, simpleMallOrderDetailVO);
            simpleMallOrderDetailVO.setOrderStatusString(SimpleMallOrderStatusEnum.getSimpleMallOrderStatusEnumByStatus(simpleMallOrderDetailVO.getOrderStatus()).getName());
            simpleMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(simpleMallOrderDetailVO.getPayType()).getName());
            simpleMallOrderDetailVO.setSimpleMallOrderItemVOS(simpleMallOrderItemVOS);
            return simpleMallOrderDetailVO;
        } else {
            SimpleMallException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
            return null;
        }
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = simpleMallOrderMapper.getTotalSimpleMallOrders(pageUtil);
        List<SimpleMallOrder> simpleMallOrders = simpleMallOrderMapper.findSimpleMallOrderList(pageUtil);
        List<SimpleMallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(simpleMallOrders, SimpleMallOrderListVO.class);
            //设置订单状态中文显示值
            for (SimpleMallOrderListVO simpleMallOrderListVO : orderListVOS) {
                simpleMallOrderListVO.setOrderStatusString(SimpleMallOrderStatusEnum.getSimpleMallOrderStatusEnumByStatus(simpleMallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = simpleMallOrders.stream().map(SimpleMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<SimpleMallOrderItem> orderItems = simpleMallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<SimpleMallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(SimpleMallOrderItem::getOrderId));
                for (SimpleMallOrderListVO simpleMallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(simpleMallOrderListVO.getOrderId())) {
                        List<SimpleMallOrderItem> orderItemListTemp = itemByOrderIdMap.get(simpleMallOrderListVO.getOrderId());
                        //将SimpleMallOrderItem对象列表转换成SimpleMallOrderItemVO对象列表
                        List<SimpleMallOrderItemVO> simpleMallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, SimpleMallOrderItemVO.class);
                        simpleMallOrderListVO.setSimpleMallOrderItemVOS(simpleMallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        SimpleMallOrder simpleMallOrder = simpleMallOrderMapper.selectByOrderNo(orderNo);
        if (simpleMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (simpleMallOrderMapper.closeOrder(Collections.singletonList(simpleMallOrder.getOrderId()), SimpleMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        SimpleMallOrder simpleMallOrder = simpleMallOrderMapper.selectByOrderNo(orderNo);
        if (simpleMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            simpleMallOrder.setOrderStatus((byte) SimpleMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            simpleMallOrder.setUpdateTime(new Date());
            if (simpleMallOrderMapper.updateByPrimaryKeySelective(simpleMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        SimpleMallOrder simpleMallOrder = simpleMallOrderMapper.selectByOrderNo(orderNo);
        if (simpleMallOrder != null) {
            if (simpleMallOrder.getOrderStatus().intValue() != SimpleMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                SimpleMallException.fail("非待支付状态下的订单无法支付");
            }
            simpleMallOrder.setOrderStatus((byte) SimpleMallOrderStatusEnum.OREDER_PAID.getOrderStatus());
            simpleMallOrder.setPayType((byte) payType);
            simpleMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            simpleMallOrder.setPayTime(new Date());
            simpleMallOrder.setUpdateTime(new Date());
            if (simpleMallOrderMapper.updateByPrimaryKeySelective(simpleMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public Boolean deleteById(Long id){
        return simpleMallOrderMapper.deleteByPrimaryKey(id) > 0;
    }
    @Override
    public Boolean deleteBatch(Long ids[]){
        if(ids.length < 0) return false;
        return simpleMallOrderMapper.deleteBatch(ids) > 0;
    }

    @Override
    @Transactional
    public String saveOrder(MallUser loginMallUser, MallUserAddress address, List<SimpleMallShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(SimpleMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(SimpleMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<SimpleMallGoods> simpleMallGoods = simpleMallGoodsMapper.selectByPrimaryKeys(goodsIds);
        //检查是否包含已下架商品
        List<SimpleMallGoods> goodsListNotSelling = simpleMallGoods.stream()
                .filter(simpleMallGoodsTemp -> simpleMallGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            SimpleMallException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, SimpleMallGoods> simpleMallGoodsMap = simpleMallGoods.stream().collect(Collectors.toMap(SimpleMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (SimpleMallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!simpleMallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                SimpleMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > simpleMallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                SimpleMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(simpleMallGoods)) {
            if (simpleMallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = simpleMallGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    SimpleMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                SimpleMallOrder simpleMallOrder = new SimpleMallOrder();
                simpleMallOrder.setOrderNo(orderNo);
                simpleMallOrder.setUserId(loginMallUser.getUserId());
                //总价
                for (SimpleMallShoppingCartItemVO simpleMallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += simpleMallShoppingCartItemVO.getGoodsCount() * simpleMallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    SimpleMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                simpleMallOrder.setTotalPrice(priceTotal);
                String extraInfo = "";
                simpleMallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (simpleMallOrderMapper.insertSelective(simpleMallOrder) > 0) {
                    //生成订单收货地址快照，并保存至数据库
                    SimpleMallOrderAddress simpleMallOrderAddress = new SimpleMallOrderAddress();
                    BeanUtil.copyProperties(address, simpleMallOrderAddress);
                    simpleMallOrderAddress.setOrderId(simpleMallOrder.getOrderId());
                    //生成所有的订单项快照，并保存至数据库
                    List<SimpleMallOrderItem> simpleMallOrderItems = new ArrayList<>();
                    for (SimpleMallShoppingCartItemVO simpleMallShoppingCartItemVO : myShoppingCartItems) {
                        SimpleMallOrderItem simpleMallOrderItem = new SimpleMallOrderItem();
                        //使用BeanUtil工具类将simpleMallShoppingCartItemVO中的属性复制到simpleMallOrderItem对象中
                        BeanUtil.copyProperties(simpleMallShoppingCartItemVO, simpleMallOrderItem);
                        //SimpleMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        simpleMallOrderItem.setOrderId(simpleMallOrder.getOrderId());
                        simpleMallOrderItems.add(simpleMallOrderItem);
                    }
                    //保存至数据库
                    if (simpleMallOrderItemMapper.insertBatch(simpleMallOrderItems) > 0 && simpleMallOrderAddressMapper.insertSelective(simpleMallOrderAddress) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    SimpleMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                SimpleMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            SimpleMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        SimpleMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }
}
