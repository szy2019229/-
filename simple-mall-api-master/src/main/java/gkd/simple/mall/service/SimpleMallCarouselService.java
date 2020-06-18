
package gkd.simple.mall.service;
import gkd.simple.mall.api.mall.vo.SimpleMallIndexCarouselVO;
import gkd.simple.mall.entity.Carousel;

import java.util.List;

public interface SimpleMallCarouselService {

    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<SimpleMallIndexCarouselVO> getCarouselsForIndex(int number);

    List<Carousel> getCarousels();

    Carousel getCarouselById(Integer carouselId);
    Boolean insertCarousel(SimpleMallIndexCarouselVO simpleMallIndexCarouselVO);
    Boolean updateCarousel(SimpleMallIndexCarouselVO simpleMallIndexCarouselVO, Integer carouselId);
    Boolean deleteCarousel(Integer carouselId);
}
