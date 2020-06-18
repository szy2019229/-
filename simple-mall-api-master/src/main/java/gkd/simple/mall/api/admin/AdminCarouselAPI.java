
package gkd.simple.mall.api.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import gkd.simple.mall.api.mall.vo.SimpleMallIndexCarouselVO;
import gkd.simple.mall.config.annotation.TokenToAdminUser;
import gkd.simple.mall.entity.AdminUser;
import gkd.simple.mall.entity.Carousel;
import gkd.simple.mall.service.SimpleMallCarouselService;
import gkd.simple.mall.util.Result;
import gkd.simple.mall.util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "0xa.后台管理轮播图相关接口")
@RequestMapping("/api/v1/admin")
public class AdminCarouselAPI {

    @Resource
    private SimpleMallCarouselService simpleMallCarouselService;

    private static final Logger logger = LoggerFactory.getLogger(AdminCarouselAPI.class);

    @GetMapping("/carousels")
    @ApiOperation(value = "获取轮播图列表", notes = "")
    public Result<List<Carousel>> getCarouselList( @TokenToAdminUser AdminUser loginAdminUser) {
        List<Carousel> carousels = simpleMallCarouselService.getCarousels();
        if(carousels!=null)
            return ResultGenerator.genSuccessResult(carousels);
        else {
            Result result = ResultGenerator.genFailResult("查询失败");
            return result;
        }
    }

    @GetMapping("/carousel/{carouselId}")
    @ApiOperation(value = "根据id获取一个轮播图", notes = "")
    public Result<Carousel> getCarousel(@PathVariable Integer carouselId, @TokenToAdminUser AdminUser loginAdminUser) {
        Carousel carousel = simpleMallCarouselService.getCarouselById(carouselId);
        if(carousel !=null){
            return ResultGenerator.genSuccessResult(carousel);
        }
        else {
            //返回失败
            Result result = ResultGenerator.genFailResult("查询失败");
            return result;
        }
    }

    @PostMapping("/carousel")
    @ApiOperation(value = "新增轮播图", notes = "")
    public Result insertCarousel(@RequestBody @ApiParam("轮播图信息") SimpleMallIndexCarouselVO simpleMallIndexCarouselVO, @TokenToAdminUser AdminUser loginAdminUser) {
        Boolean flag = simpleMallCarouselService.insertCarousel(simpleMallIndexCarouselVO);
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("添加失败");
            return result;
        }
    }

    @PutMapping("/carousel/{carouselId}")
    @ApiOperation(value = "根据id修改轮播图信息", notes = "")
    public Result updateCarousel(@RequestBody @ApiParam("轮播图信息") SimpleMallIndexCarouselVO simpleMallIndexCarouselVO, @PathVariable Integer carouselId, @TokenToAdminUser AdminUser loginAdminUser) {
        Boolean flag = simpleMallCarouselService.updateCarousel(simpleMallIndexCarouselVO, carouselId);
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

    @DeleteMapping("/carousel/{carouselId}")
    @ApiOperation(value = "根据id删除轮播图信息", notes = "")
    public Result deleteCarousel(@PathVariable Integer carouselId, @TokenToAdminUser AdminUser loginAdminUser) {
        Boolean flag = simpleMallCarouselService.deleteCarousel(carouselId);
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("删除失败");
            return result;
        }
    }

}
