
package gkd.simple.mall.service;

import gkd.simple.mall.entity.SimpleMallGoods;
import gkd.simple.mall.util.PageQueryUtil;
import gkd.simple.mall.util.PageResult;

import java.util.List;

public interface SimpleMallGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getSimpleMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveSimpleMallGoods(SimpleMallGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param simpleMallGoodsList
     * @return
     */
    void batchSaveSimpleMallGoods(List<SimpleMallGoods> simpleMallGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateSimpleMallGoods(SimpleMallGoods goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    SimpleMallGoods getSimpleMallGoodsById(Long id);
    Boolean deleteById(Long id);
    Boolean deleteBatch(Integer[] ids);
    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchSimpleMallGoods(PageQueryUtil pageUtil);

    List<SimpleMallGoods> getGoods();

    PageResult getGoods(PageQueryUtil pageUtil);
}
