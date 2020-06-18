
package gkd.simple.mall.api.mall;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import gkd.simple.mall.common.SimpleMallException;
import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.api.mall.vo.SimpleMallIndexCategoryVO;
import gkd.simple.mall.service.SimpleMallCategoryService;
import gkd.simple.mall.util.Result;
import gkd.simple.mall.util.ResultGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "0x3.Simple商城分类页面接口")
@RequestMapping("/api/v1")
public class SimpleMallGoodsCategoryAPI {

    @Resource
    private SimpleMallCategoryService simpleMallCategoryService;

    @GetMapping("/categories")
    @ApiOperation(value = "获取分类数据", notes = "分类页面使用")
    public Result<List<SimpleMallIndexCategoryVO>> getCategories() {
        List<SimpleMallIndexCategoryVO> categories = simpleMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            SimpleMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(categories);
    }
}
