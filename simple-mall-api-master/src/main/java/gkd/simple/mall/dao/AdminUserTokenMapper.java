
package gkd.simple.mall.dao;

import gkd.simple.mall.entity.AdminUserToken;
import org.apache.ibatis.annotations.Param;

public interface AdminUserTokenMapper {
    int deleteByPrimaryKey(@Param("adminUserId") Long adminUserId);

    int insert(AdminUserToken record);

    int insertSelective(AdminUserToken record);

    AdminUserToken selectByPrimaryKey(Long adminUserId);

    AdminUserToken selectByToken(String token);

    int updateByPrimaryKeySelective(AdminUserToken record);

    int updateByPrimaryKey(AdminUserToken record);
}