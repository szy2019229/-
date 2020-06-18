
package gkd.simple.mall.service.impl;

import gkd.simple.mall.api.mall.vo.SimpleMallIndexConfigGoodsVO;
import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.dao.IndexConfigMapper;
import gkd.simple.mall.dao.SimpleMallGoodsMapper;
import gkd.simple.mall.entity.IndexConfig;
import gkd.simple.mall.entity.SimpleMallGoods;
import gkd.simple.mall.service.SimpleMallIndexConfigService;
import gkd.simple.mall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimpleMallIndexConfigServiceImpl implements SimpleMallIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private SimpleMallGoodsMapper goodsMapper;

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return indexConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean deleteById(Long id){
        return indexConfigMapper.deleteByPrimaryKey(id) > 0;
    }
    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return indexConfigMapper.deleteBatch(ids) > 0;
    }

    public List<IndexConfig> getConfigs(int configType){
        return indexConfigMapper.getIndexConfigs(configType);
    }

    @Override
    public List<SimpleMallIndexConfigGoodsVO> getConfigGoodsForIndex(int configType, int number) {
        List<SimpleMallIndexConfigGoodsVO> simpleMallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<SimpleMallGoods> simpleMallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            simpleMallIndexConfigGoodsVOS = BeanUtil.copyList(simpleMallGoods, SimpleMallIndexConfigGoodsVO.class);
            for (SimpleMallIndexConfigGoodsVO simpleMallIndexConfigGoodsVO : simpleMallIndexConfigGoodsVOS) {
                String goodsName = simpleMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = simpleMallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    simpleMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    simpleMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return simpleMallIndexConfigGoodsVOS;
    }
}
