package gkd.simple.mall.api.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import gkd.simple.mall.api.mall.vo.SimpleMallIndexCategoryVO;
import gkd.simple.mall.common.SimpleMallCategoryLevelEnum;
import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.config.annotation.TokenToAdminUser;
import gkd.simple.mall.entity.AdminUser;
import gkd.simple.mall.entity.GoodsCategory;
import gkd.simple.mall.service.SimpleMallCategoryService;
import gkd.simple.mall.util.Result;
import gkd.simple.mall.util.ResultGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@Api(value = "v1", tags = "0xc.后台管理系统商品类别操作")
@RequestMapping("/api/v1/admin")

public class AdminGoodsCategoryAPI {
    @Resource
    private SimpleMallCategoryService simpleMallCategoryService;

        @GetMapping("/categories")
        public Result<List<SimpleMallIndexCategoryVO>> categoriesPage(@TokenToAdminUser AdminUser loginAdminUser){
            List<SimpleMallIndexCategoryVO>categoryResult = simpleMallCategoryService.getCategoriesForIndex();
            if(categoryResult == null)
                return ResultGenerator.genFailResult("查询失败，没有记录");
            return ResultGenerator.genSuccessResult(categoryResult);
        }

    /**
     * 列表
     */
    @GetMapping("/categories/{categoryId}")
    @ApiOperation(value = "列表", notes = "列表")
    public Result listForSelect(@PathVariable Long categoryId, @TokenToAdminUser AdminUser loginAdminUser) {
        if (categoryId == null || categoryId < 1) {
            return ResultGenerator.genFailResult("缺少参数！");
        }
        GoodsCategory category = simpleMallCategoryService.getGoodsCategoryById(categoryId);
        //既不是一级分类也不是二级分类则为不返回数据
        if (category == null || category.getCategoryLevel() == SimpleMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map categoryResult = new HashMap(2);
        if (category.getCategoryLevel() == SimpleMallCategoryLevelEnum.LEVEL_ONE.getLevel()) {
            //如果是一级分类则返回当前一级分类下的所有二级分类，以及二级分类列表中第一条数据下的所有三级分类列表
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), SimpleMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), SimpleMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                categoryResult.put("secondLevelCategories", secondLevelCategories);
                categoryResult.put("thirdLevelCategories", thirdLevelCategories);
            }
        }
        if (category.getCategoryLevel() == SimpleMallCategoryLevelEnum.LEVEL_TWO.getLevel()) {
            //如果是二级分类则返回当前分类下的所有三级分类列表
            List<GoodsCategory> thirdLevelCategories = simpleMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), SimpleMallCategoryLevelEnum.LEVEL_THREE.getLevel());
            categoryResult.put("thirdLevelCategories", thirdLevelCategories);
        }
        return ResultGenerator.genSuccessResult(categoryResult);
    }

    /**
     * 添加
     */
    @PostMapping("/category")
    @ApiOperation(value = "添加", notes = "添加")
    public Result save(@RequestBody GoodsCategory goodsCategory, @TokenToAdminUser AdminUser loginAdminUser) {
        if (Objects.isNull(goodsCategory.getCategoryLevel())
                || StringUtils.isEmpty(goodsCategory.getCategoryName())
                || Objects.isNull(goodsCategory.getParentId())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = simpleMallCategoryService.saveCategory(goodsCategory);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     */
    @PutMapping("/category")
    @ApiOperation(value = "修改", notes = "修改")
    public Result update(@RequestBody GoodsCategory goodsCategory, @TokenToAdminUser AdminUser loginAdminUser) {
        if (Objects.isNull(goodsCategory.getCategoryId())
                || Objects.isNull(goodsCategory.getCategoryLevel())
                || StringUtils.isEmpty(goodsCategory.getCategoryName())
                || Objects.isNull(goodsCategory.getParentId())
                || Objects.isNull(goodsCategory.getCategoryRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = simpleMallCategoryService.updateGoodsCategory(goodsCategory);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }




    /**
     * 详情
     */
    @GetMapping("/category/{categoryId}")
    @ApiOperation(value = "详情", notes = "详情")
    public Result info(@PathVariable("categoryId") Long categoryId, @TokenToAdminUser AdminUser loginAdminUser) {
        GoodsCategory goodsCategory = simpleMallCategoryService.getGoodsCategoryById(categoryId);
        if (goodsCategory == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(goodsCategory);
    }

    /**
     * 分类删除
     */
    @DeleteMapping("/category")
    @ApiOperation(value = "分类删除", notes = "分类删除")
    public Result delete(@RequestBody Integer[] categoryIds, @TokenToAdminUser AdminUser loginAdminUser) {
        if (categoryIds.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (simpleMallCategoryService.deleteBatch(categoryIds)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }


}
