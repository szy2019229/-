package gkd.simple.mall.service.impl;

import gkd.simple.mall.api.admin.param.AdminUserUpdateParam;
import gkd.simple.mall.common.SimpleMallException;
import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.entity.AdminUser;
import gkd.simple.mall.dao.AdminUserMapper;
import gkd.simple.mall.dao.AdminUserTokenMapper;
import gkd.simple.mall.entity.AdminUserToken;
import gkd.simple.mall.service.AdminUserService;
import gkd.simple.mall.util.NumberUtil;
import gkd.simple.mall.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private AdminUserTokenMapper adminUserTokenMapper;

    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }

    @Override
    public String login(String loginName, String passwordMd5) {
        AdminUser adminUser = adminUserMapper.login(loginName, passwordMd5);
        if (adminUser != null) {
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", adminUser.getAdminUserId());
            AdminUserToken adminUserToken = adminUserTokenMapper.selectByPrimaryKey(adminUser.getAdminUserId());
            //当前时间
            Date now = new Date();
            //过期时间
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
            if (adminUserToken == null) {
                adminUserToken = new AdminUserToken();
                adminUserToken.setAdminUserId(adminUser.getAdminUserId());
                adminUserToken.setToken(token);
                adminUserToken.setUpdateTime(now);
                adminUserToken.setExpireTime(expireTime);
                //新增一条token数据
                if (adminUserTokenMapper.insertSelective(adminUserToken) > 0) {
                    //新增成功后返回
                    return token;
                }
            } else {
                adminUserToken.setToken(token);
                adminUserToken.setUpdateTime(now);
                adminUserToken.setExpireTime(expireTime);
                //更新
                if (adminUserTokenMapper.updateByPrimaryKeySelective(adminUserToken) > 0) {
                    //修改成功后返回
                    return token;
                }
            }
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }
    @Override
    public Boolean logout(Long userId) {
        boolean a = (adminUserTokenMapper.deleteByPrimaryKey(userId) > 0);
        System.out.println(a);
        return a;
    }
    @Override
    public Boolean updateUserInfo(AdminUserUpdateParam adminUser, Long adminUserId) {
        AdminUser user = adminUserMapper.selectByPrimaryKey(adminUserId);
        if (user == null) {
            SimpleMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        user.setNickName(adminUser.getNickName());
        user.setPasswordMd5(adminUser.getPasswordMd5());
        if (adminUserMapper.updateByPrimaryKeySelective(user) > 0) {
            return true;
        }
        return false;
    }

}
