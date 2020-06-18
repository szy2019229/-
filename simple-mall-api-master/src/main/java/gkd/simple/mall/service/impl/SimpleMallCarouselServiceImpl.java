
package gkd.simple.mall.service.impl;

import gkd.simple.mall.api.mall.vo.SimpleMallIndexCarouselVO;
import gkd.simple.mall.common.SimpleMallException;
import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.dao.CarouselMapper;
import gkd.simple.mall.entity.Carousel;
import gkd.simple.mall.service.SimpleMallCarouselService;
import gkd.simple.mall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleMallCarouselServiceImpl implements SimpleMallCarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<SimpleMallIndexCarouselVO> getCarouselsForIndex(int number) {
        List<SimpleMallIndexCarouselVO> simpleMallIndexCarouselVOS = new ArrayList<>(number);
        List<Carousel> carousels = carouselMapper.findCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(carousels)) {
            simpleMallIndexCarouselVOS = BeanUtil.copyList(carousels, SimpleMallIndexCarouselVO.class);
        }
        return simpleMallIndexCarouselVOS;
    }

    @Override
    public List<Carousel> getCarousels() {
        return carouselMapper.getCarousels();
    }

    @Override
    public Carousel getCarouselById(Integer carouselId){
        return carouselMapper.selectByPrimaryKey(carouselId);
    }

    @Override
    public Boolean insertCarousel(SimpleMallIndexCarouselVO simpleMallIndexCarouselVO){
        Carousel carousel = new Carousel();
        carousel.setCarouselRank(simpleMallIndexCarouselVO.getCarouselRank() );
        carousel.setCarouselUrl(simpleMallIndexCarouselVO.getCarouselUrl());
        carousel.setRedirectUrl(simpleMallIndexCarouselVO.getRedirectUrl());
        return carouselMapper.insertSelective(carousel) > 0;
    }

    @Override
    public Boolean updateCarousel(SimpleMallIndexCarouselVO simpleMallIndexCarouselVO, Integer carouselId) {
        Carousel carousel = carouselMapper.selectByPrimaryKey(carouselId);
        if (carousel == null) {
            SimpleMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        carousel.setCarouselRank(simpleMallIndexCarouselVO.getCarouselRank() );
        carousel.setCarouselUrl(simpleMallIndexCarouselVO.getCarouselUrl());
        carousel.setRedirectUrl(simpleMallIndexCarouselVO.getRedirectUrl());
        return carouselMapper.updateByPrimaryKeySelective(carousel) > 0;
    }

    @Override
    public Boolean deleteCarousel(Integer carouselId){
        return carouselMapper.deleteByPrimaryKey(carouselId) > 0;
    }
}
