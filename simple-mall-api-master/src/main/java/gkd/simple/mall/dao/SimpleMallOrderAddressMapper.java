
package gkd.simple.mall.dao;

import gkd.simple.mall.entity.SimpleMallOrderAddress;

public interface SimpleMallOrderAddressMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(SimpleMallOrderAddress record);

    int insertSelective(SimpleMallOrderAddress record);

    SimpleMallOrderAddress selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(SimpleMallOrderAddress record);

    int updateByPrimaryKey(SimpleMallOrderAddress record);
}