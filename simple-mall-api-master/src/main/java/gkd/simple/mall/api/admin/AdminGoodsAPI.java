
package gkd.simple.mall.api.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiParam;
import gkd.simple.mall.common.Constants;
import gkd.simple.mall.common.SimpleMallCategoryLevelEnum;
import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.config.annotation.TokenToAdminUser;
import gkd.simple.mall.entity.AdminUser;
import gkd.simple.mall.entity.GoodsCategory;
import gkd.simple.mall.entity.SimpleMallGoods;
import gkd.simple.mall.service.SimpleMallCategoryService;
import gkd.simple.mall.service.SimpleMallGoodsService;
import gkd.simple.mall.util.PageQueryUtil;
import gkd.simple.mall.util.Result;
import gkd.simple.mall.util.ResultGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@Api(value = "v1", tags = "0xb.后台管理系统商品相关操作")
@RequestMapping("/api/v1/admin")
public class AdminGoodsAPI {
    @Resource
    private SimpleMallGoodsService simpleMallGoodsService;
    @Resource
    private SimpleMallCategoryService simpleMallCategoryService;

    @GetMapping("/goods")
    @ApiOperation(value = "查看商品", notes = "列出所有商品")
    public Result goodsPage(@TokenToAdminUser AdminUser loginAdminUser){

        return ResultGenerator.genSuccessResult(simpleMallGoodsService.getGoods());
    }

    @GetMapping("/good/edit")
    public Result edit(@TokenToAdminUser AdminUser loginAdminUser){
        Map goodsResult = new HashMap(3);
        //查询所有的一级分类
        List<GoodsCategory> firstLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), SimpleMallCategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), SimpleMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), SimpleMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                goodsResult.put("firstLevelCategories", firstLevelCategories);
                goodsResult.put("secondLevelCategories", secondLevelCategories);
                goodsResult.put("thirdLevelCategories", thirdLevelCategories);
                return ResultGenerator.genSuccessResult(goodsResult);
            }
        }
        return ResultGenerator.genErrorResult(500,"发生异常");
    }

    @GetMapping("/good/edit/{goodId}")
    public Result edit(@PathVariable("goodId") Long goodId,@TokenToAdminUser AdminUser loginAdminUser) {
        Map goodsResult = new HashMap(6);
        SimpleMallGoods simpleMallGoods = simpleMallGoodsService.getSimpleMallGoodsById(goodId);
        if (simpleMallGoods == null) {
            return ResultGenerator.genErrorResult(400,"没有商品");
        }
        if (simpleMallGoods.getGoodsCategoryId() > 0) {
            if (simpleMallGoods.getGoodsCategoryId() != null || simpleMallGoods.getGoodsCategoryId() > 0) {
                //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
                GoodsCategory currentGoodsCategory = simpleMallCategoryService.getGoodsCategoryById(simpleMallGoods.getGoodsCategoryId());
                //商品表中存储的分类id字段为三级分类的id，不为三级分类则是错误数据
                if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == SimpleMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
                    //查询所有的一级分类
                    List<GoodsCategory> firstLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), SimpleMallCategoryLevelEnum.LEVEL_ONE.getLevel());
                    //根据parentId查询当前parentId下所有的三级分类
                    List<GoodsCategory> thirdLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()), SimpleMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    //查询当前三级分类的父级二级分类
                    GoodsCategory secondCategory = simpleMallCategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (secondCategory != null) {
                        //根据parentId查询当前parentId下所有的二级分类
                        List<GoodsCategory> secondLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()), SimpleMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                        //查询当前二级分类的父级一级分类
                        GoodsCategory firestCategory = simpleMallCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if (firestCategory != null) {
                            //所有分类数据都得到之后放到request对象中供前端读取
                            goodsResult.put("firstLevelCategories", firstLevelCategories);
                            goodsResult.put("secondLevelCategories", secondLevelCategories);
                            goodsResult.put("thirdLevelCategories", thirdLevelCategories);
                            goodsResult.put("firstLevelCategoryId", firestCategory.getCategoryId());
                            goodsResult.put("secondLevelCategoryId", secondCategory.getCategoryId());
                            goodsResult.put("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (simpleMallGoods.getGoodsCategoryId() == 0) {
            //查询所有的一级分类
            List<GoodsCategory> firstLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), SimpleMallCategoryLevelEnum.LEVEL_ONE.getLevel());
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //查询一级分类列表中第一个实体的所有二级分类
                List<GoodsCategory> secondLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), SimpleMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<GoodsCategory> thirdLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), SimpleMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    goodsResult.put("firstLevelCategories", firstLevelCategories);
                    goodsResult.put("secondLevelCategories", secondLevelCategories);
                    goodsResult.put("thirdLevelCategories", thirdLevelCategories);
                }
            }
        }
        goodsResult.put("goods", simpleMallGoods);
        return ResultGenerator.genSuccessResult(goodsResult);
    }

    @GetMapping("/good/list")
    @ApiOperation(value="展示商品（分页）",notes="")
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(simpleMallGoodsService.getSimpleMallGoodsPage(pageUtil));
    }

    /**
     * 添加
     */
    @PostMapping("/good")
    @ApiOperation(value = "添加商品", notes = "")
    public Result save(@RequestBody @ApiParam("新商品") SimpleMallGoods simpleMallGoods,@TokenToAdminUser AdminUser loginAdminUser) {
        if (StringUtils.isEmpty(simpleMallGoods.getGoodsName())
                || StringUtils.isEmpty(simpleMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(simpleMallGoods.getTag())
                || Objects.isNull(simpleMallGoods.getOriginalPrice())
                || Objects.isNull(simpleMallGoods.getSellingPrice())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = simpleMallGoodsService.saveSimpleMallGoods(simpleMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改
     */
    @PutMapping("/good")
    @ApiOperation(value = "修改商品", notes = "")
    public Result update(@RequestBody SimpleMallGoods simpleMallGoods,@TokenToAdminUser AdminUser loginAdminUser) {
        if (StringUtils.isEmpty(simpleMallGoods.getGoodsName())
                || StringUtils.isEmpty(simpleMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(simpleMallGoods.getTag())
                || Objects.isNull(simpleMallGoods.getOriginalPrice())
                || Objects.isNull(simpleMallGoods.getSellingPrice())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = simpleMallGoodsService.updateSimpleMallGoods(simpleMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/good/{goodId}")
    @ApiOperation(value = "商品详情", notes = "")
    public Result info(@PathVariable("goodId") Long goodId,@TokenToAdminUser AdminUser loginAdminUser) {
        SimpleMallGoods good = simpleMallGoodsService.getSimpleMallGoodsById(goodId);
        if (good == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(good);
    }
    /**
     * 删除
     */
    @DeleteMapping("/good/{goodId}")
    @ApiOperation(value = "根据id数组删除商品", notes = "")
    public Result deleteGoods(@PathVariable Long goodId ,@TokenToAdminUser AdminUser loginAdminUser) {
        if (simpleMallGoodsService.deleteById(goodId)) {
            return ResultGenerator.genSuccessResult("删除成功");
        }
        return ResultGenerator.genFailResult("删除失败");
    }

    /**
     * 批量修改销售状态
     */
    @PutMapping("/good/status/{sellStatus}")
    @ApiOperation(value = "批量修改销售状态", notes = "批量修改销售状态")
    public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus,@TokenToAdminUser AdminUser loginAdminUser) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (simpleMallGoodsService.batchUpdateSellStatus(ids, sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }
}
