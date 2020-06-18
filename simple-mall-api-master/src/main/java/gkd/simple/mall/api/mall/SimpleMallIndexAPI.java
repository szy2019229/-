
package gkd.simple.mall.api.mall;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import gkd.simple.mall.common.Constants;
import gkd.simple.mall.common.IndexConfigTypeEnum;
import gkd.simple.mall.api.mall.vo.IndexInfoVO;
import gkd.simple.mall.api.mall.vo.SimpleMallIndexCarouselVO;
import gkd.simple.mall.api.mall.vo.SimpleMallIndexConfigGoodsVO;
import gkd.simple.mall.service.SimpleMallCarouselService;
import gkd.simple.mall.service.SimpleMallIndexConfigService;
import gkd.simple.mall.util.Result;
import gkd.simple.mall.util.ResultGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "0x1.Simple商城首页接口")
@RequestMapping("/api/v1")
public class SimpleMallIndexAPI {

    @Resource
    private SimpleMallCarouselService simpleMallCarouselService;

    @Resource
    private SimpleMallIndexConfigService simpleMallIndexConfigService;

    @GetMapping("/index-infos")
    @ApiOperation(value = "获取首页数据", notes = "轮播图、新品、推荐等")
    public Result<IndexInfoVO> indexInfo() {
        IndexInfoVO indexInfoVO = new IndexInfoVO();
        List<SimpleMallIndexCarouselVO> carousels = simpleMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<SimpleMallIndexConfigGoodsVO> hotGoodses = simpleMallIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<SimpleMallIndexConfigGoodsVO> newGoodses = simpleMallIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<SimpleMallIndexConfigGoodsVO> recommendGoodses = simpleMallIndexConfigService.getConfigGoodsForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        indexInfoVO.setCarousels(carousels);
        indexInfoVO.setHotGoodses(hotGoodses);
        indexInfoVO.setNewGoodses(newGoodses);
        indexInfoVO.setRecommendGoodses(recommendGoodses);
        return ResultGenerator.genSuccessResult(indexInfoVO);
    }
}
