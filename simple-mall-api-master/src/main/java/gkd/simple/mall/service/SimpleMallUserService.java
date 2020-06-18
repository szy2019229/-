
package gkd.simple.mall.service;

import gkd.simple.mall.api.mall.param.MallUserUpdateParam;
import gkd.simple.mall.entity.MallUser;
import gkd.simple.mall.util.PageQueryUtil;
import gkd.simple.mall.util.PageResult;

import java.util.List;

public interface SimpleMallUserService {


    String register(String loginName, String password);

    String login(String loginName, String passwordMD5);

    MallUser getUserById(Long userId);

    Boolean insertUser(MallUser mallUser);

    Boolean updateUserInfo(MallUserUpdateParam mallUser, Long userId);

    Boolean deleteUserInfo(Long userId);

    /**
     * 登出接口
     * @param userId
     * @return
     */
    Boolean logout(Long userId);


    List<MallUser> getUsers();

    PageResult getUsers(PageQueryUtil pageUtil);
}
