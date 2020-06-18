
package gkd.simple.mall.service;

import gkd.simple.mall.api.admin.param.AdminUserUpdateParam;

public interface AdminUserService {

    String login(String userName, String password);

    public Boolean logout(Long userId) ;
    public Boolean updateUserInfo(AdminUserUpdateParam adminUser, Long adminUserId);
}
