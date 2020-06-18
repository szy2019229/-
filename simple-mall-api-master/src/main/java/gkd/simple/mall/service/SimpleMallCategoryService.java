
package gkd.simple.mall.service;

import gkd.simple.mall.api.mall.vo.SimpleMallIndexCategoryVO;
import gkd.simple.mall.entity.GoodsCategory;
import gkd.simple.mall.util.PageQueryUtil;
import gkd.simple.mall.util.PageResult;

import java.util.List;

public interface SimpleMallCategoryService {

    PageResult getCategorisPage(PageQueryUtil pageUtil);

    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory goodsCategory);

    GoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<SimpleMallIndexCategoryVO> getCategoriesForIndex();
    /**
     * 根据parentId和level获取分类列表
     *
     * @param parentIds
     * @param categoryLevel
     * @return
     */
    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);
}
