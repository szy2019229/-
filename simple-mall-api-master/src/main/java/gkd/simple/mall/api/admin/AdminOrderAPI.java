package gkd.simple.mall.api.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.config.annotation.TokenToAdminUser;
import gkd.simple.mall.entity.AdminUser;
import gkd.simple.mall.entity.SimpleMallOrder;
import gkd.simple.mall.service.SimpleMallOrderService;
import gkd.simple.mall.util.PageQueryUtil;
import gkd.simple.mall.util.Result;
import gkd.simple.mall.util.ResultGenerator;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Api(value = "v1", tags = "0xe.后台订单管理相关接口")
@RequestMapping("/api/v1/admin")
public class AdminOrderAPI {

    @Resource
    private SimpleMallOrderService simpleMallOrderService;

    @GetMapping("/orders")
    @ApiOperation(value = "查看所有订单", notes = "")
    public Result<List<SimpleMallOrder>> ordersPage(@TokenToAdminUser AdminUser loginAdminUser) {
        List<SimpleMallOrder> simpleMallOrders = simpleMallOrderService.getOrders();
        if(simpleMallOrders == null){
            return ResultGenerator.genFailResult("查询失败");
        }
        return ResultGenerator.genSuccessResult(simpleMallOrders);
    }

/**
     * 列表
     */

    @GetMapping("/orders/list")
    @ApiOperation(value = "订单列表(分页）", notes = "")
    public Result list(@RequestParam Map<String, Object> params,@TokenToAdminUser AdminUser loginAdminUser) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(simpleMallOrderService.getSimpleMallOrdersPage(pageUtil));
    }

/**
     * 修改
     */

    @PutMapping(value = "/order")
    @ApiOperation(value = "修改订单信息", notes = "")
    public Result update(@RequestBody SimpleMallOrder simpleMallOrder,@TokenToAdminUser AdminUser loginAdminUser) {
        if (Objects.isNull(simpleMallOrder.getTotalPrice())
                || Objects.isNull(simpleMallOrder.getOrderId())
                || simpleMallOrder.getOrderId() < 1
                || simpleMallOrder.getTotalPrice() < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = simpleMallOrderService.updateOrderInfo(simpleMallOrder);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 删除
     */

    @DeleteMapping(value = "/order/{orderId}")
    @ApiOperation(value = "根据id删除订单", notes = "")
    public Result delete(@PathVariable Long orderId,@TokenToAdminUser AdminUser loginAdminUser) {
        if (simpleMallOrderService.deleteById(orderId)) {
            return ResultGenerator.genSuccessResult("删除成功");
        }
        return ResultGenerator.genFailResult("删除失败");
    }

/**
     * 详情
     */

    @GetMapping("/order/{orderId}")
    @ApiOperation(value = "查看订单详情", notes = "")
    public Result info(@PathVariable Long orderId,@TokenToAdminUser AdminUser loginAdminUser) {
        SimpleMallOrder simpleMallOrder = simpleMallOrderService.getOrderByOrderId(orderId);
        if (simpleMallOrder == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());

        }
        return ResultGenerator.genSuccessResult(simpleMallOrder);
    }


/**
     * 配货
     */

    @PostMapping(value = "/order/checkDone")
    @ApiOperation(value = "查看快递状态,如果符合要求则配货", notes = "")
    public Result checkDone(@RequestBody Long[] ids,@TokenToAdminUser AdminUser loginAdminUser) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = simpleMallOrderService.checkDone(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


/**
     * 出库
     */

    @PostMapping(value = "/order/checkOut")
    @ApiOperation(value = "查看快递状态,如果符合要求则出库", notes = "")
    public Result checkOut(@RequestBody Long[] ids,@TokenToAdminUser AdminUser loginAdminUser) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = simpleMallOrderService.checkOut(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 关闭订单
     */

    @PostMapping(value = "/order/close")
    @ApiOperation(value = "关闭订单", notes = "")
    public Result closeOrder(@RequestBody Long[] ids,@TokenToAdminUser AdminUser loginAdminUser) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = simpleMallOrderService.closeOrder(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

}
