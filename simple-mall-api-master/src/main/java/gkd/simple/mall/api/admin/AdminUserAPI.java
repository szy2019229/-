
package gkd.simple.mall.api.admin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import gkd.simple.mall.api.admin.param.AdminUserLoginParam;
import gkd.simple.mall.api.admin.param.AdminUserUpdateParam;
import gkd.simple.mall.common.Constants;
import gkd.simple.mall.config.annotation.TokenToAdminUser;
import gkd.simple.mall.entity.AdminUser;
import gkd.simple.mall.service.AdminUserService;
import gkd.simple.mall.util.Result;
import gkd.simple.mall.util.ResultGenerator;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;

@RestController
@Api(value = "v1", tags = "0x8.后台管理系统管理员用户操作相关接口")
@RequestMapping("/api/v1/admin")
public class AdminUserAPI {

    @Resource
    private AdminUserService adminUserService;
    private static final Logger logger = LoggerFactory.getLogger(AdminUserAPI.class);

    @PostMapping("/login")
    @ApiOperation(value = "管理员登录接口", notes = "返回token")
    public Result<String> login(@RequestBody @Valid AdminUserLoginParam adminUserLoginParam) {
        String loginResult = adminUserService.login(adminUserLoginParam.getLoginName(), adminUserLoginParam.getPasswordMd5());
        logger.info("admin login api,loginName={},loginResult={}", adminUserLoginParam.getLoginName(), loginResult);
        //登录成功
        if (!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);
    }


    @PostMapping("/logout")
    @ApiOperation(value = "管理员登出接口", notes = "清除token")
    public Result<String> logout(@TokenToAdminUser AdminUser loginAdminUser) {
        Boolean logoutResult = adminUserService.logout(loginAdminUser.getAdminUserId());
        logger.info("admin logout api,loginAdminUser={}", loginAdminUser.getAdminUserId());
        //登出成功
        if (logoutResult) {
            System.out.println("yes!");
            return ResultGenerator.genSuccessResult();
        }
        //登出失败
        System.out.println("no!");
        return ResultGenerator.genFailResult("logout error");
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取管理员个人信息", notes = "")
    public Result<AdminUser> getUserDetail(@TokenToAdminUser AdminUser loginAdminUser) {
        //已登录则直接返回
        return ResultGenerator.genSuccessResult(loginAdminUser);
    }

    @PutMapping("/info")
    @ApiOperation(value = "修改管理员个人信息", notes = "")
    public Result updateInfo(@RequestBody @ApiParam("用户信息") AdminUserUpdateParam adminUserUpdateParam, @TokenToAdminUser AdminUser loginAdminUser) {
        Boolean flag = adminUserService.updateUserInfo(adminUserUpdateParam, loginAdminUser.getAdminUserId());
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

}
