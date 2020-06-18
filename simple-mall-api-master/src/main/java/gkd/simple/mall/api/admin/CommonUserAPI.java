
package gkd.simple.mall.api.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import gkd.simple.mall.api.mall.param.MallUserUpdateParam;
import gkd.simple.mall.config.annotation.TokenToAdminUser;
import gkd.simple.mall.entity.AdminUser;
import gkd.simple.mall.entity.MallUser;
import gkd.simple.mall.service.SimpleMallUserService;
import gkd.simple.mall.util.Result;
import gkd.simple.mall.util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "0x9.后台管理普通用户相关接口")
@RequestMapping("/api/v1/admin")
public class CommonUserAPI {

    @Resource
    private SimpleMallUserService simpleMallUserService;

    private static final Logger logger = LoggerFactory.getLogger(CommonUserAPI.class);

    @GetMapping("/users")
    @ApiOperation(value = "获取普通用户列表", notes = "")
    public Result getUserList(@TokenToAdminUser AdminUser loginAdminUser) {
        List<MallUser> mallUsers = simpleMallUserService.getUsers();
        if(mallUsers!=null)
            return ResultGenerator.genSuccessResult(mallUsers);
        else {
            Result result = ResultGenerator.genFailResult("查询失败");
            return result;
        }
    }

    @GetMapping("/user/{userId}")
    @ApiOperation(value = "根据id获取一个普通用户", notes = "")
    public Result<MallUser> getUserList(@TokenToAdminUser AdminUser loginAdminUser, @PathVariable Long userId) {
        MallUser mallUser = simpleMallUserService.getUserById(userId);
        if(mallUser !=null){
            return ResultGenerator.genSuccessResult(mallUser);
        }
        else {
            //返回失败
            Result result = ResultGenerator.genFailResult("查询失败");
            return result;
        }
    }

    @PostMapping("/user")
    @ApiOperation(value = "新增普通用户信息", notes = "")
    public Result insertUser(@RequestBody  MallUser mallUser, @TokenToAdminUser AdminUser loginAdminUser) {
        Boolean flag = simpleMallUserService.insertUser(mallUser);
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("添加失败");
            return result;
        }
    }

    @PutMapping("/user/{userId}")
    @ApiOperation(value = "根据id修改普通用户信息", notes = "")
    public Result updateUserInfo(@RequestBody  MallUserUpdateParam mallUserUpdateParam, @PathVariable Long userId, @TokenToAdminUser AdminUser loginAdminUser) {
        Boolean flag = simpleMallUserService.updateUserInfo(mallUserUpdateParam, userId);
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

    @DeleteMapping("/user/{userId}")
    @ApiOperation(value = "根据id删除普通用户信息", notes = "")
    public Result deleteUserInfo(@PathVariable Long userId, @TokenToAdminUser AdminUser loginAdminUser) {
        Boolean flag = simpleMallUserService.deleteUserInfo(userId);
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("删除失败");
            return result;
        }
    }

}
