
package gkd.simple.mall.service;

import gkd.simple.mall.api.mall.vo.SimpleMallIndexConfigGoodsVO;
import gkd.simple.mall.entity.IndexConfig;

import java.util.List;

public interface SimpleMallIndexConfigService {

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    Boolean deleteBatch(Long[] ids);

    Boolean deleteById(Long id);

    List<IndexConfig> getConfigs(int configType) ;

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<SimpleMallIndexConfigGoodsVO> getConfigGoodsForIndex(int configType, int number);
}
