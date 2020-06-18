
package gkd.simple.mall.dao;

import gkd.simple.mall.entity.SimpleMallGoods;
import gkd.simple.mall.entity.StockNumDTO;
import gkd.simple.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SimpleMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int deleteBatch(Integer[] ids);

    int insert(SimpleMallGoods record);

    int insertSelective(SimpleMallGoods record);

    SimpleMallGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(SimpleMallGoods record);

    int updateByPrimaryKeyWithBLOBs(SimpleMallGoods record);

    int updateByPrimaryKey(SimpleMallGoods record);

    List<SimpleMallGoods> findSimpleMallGoodsList(PageQueryUtil pageUtil);

    int getTotalSimpleMallGoods(PageQueryUtil pageUtil);

    List<SimpleMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<SimpleMallGoods> findSimpleMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalSimpleMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("simpleMallGoodsList") List<SimpleMallGoods> simpleMallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

    List<SimpleMallGoods> getGoods();

}