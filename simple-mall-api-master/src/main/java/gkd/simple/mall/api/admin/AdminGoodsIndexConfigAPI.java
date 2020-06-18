package gkd.simple.mall.api.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.config.annotation.TokenToAdminUser;
import gkd.simple.mall.entity.AdminUser;
import gkd.simple.mall.entity.IndexConfig;
import gkd.simple.mall.service.SimpleMallIndexConfigService;
import gkd.simple.mall.util.Result;
import gkd.simple.mall.util.ResultGenerator;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@Api(value = "v1", tags = "0xd.后台管理员热销商品、新品上线、为你推荐相关接口")
@RequestMapping("/api/v1/admin")
public class AdminGoodsIndexConfigAPI {


    @Resource
    private SimpleMallIndexConfigService simpleMallIndexConfigService;

    @GetMapping("/indexConfigs/configType/{typeId}")
    @ApiOperation(value = "根据configType展示对应的所有配置项", notes = "")
    public Result<List<IndexConfig>> indexConfigsPage(@PathVariable int typeId, @TokenToAdminUser AdminUser loginAdminUser) {
        List<IndexConfig> indexConfig = simpleMallIndexConfigService.getConfigs(typeId);
        if (indexConfig == null) {
            return ResultGenerator.genFailResult("查询失败，没有找到配置");
        }
        return ResultGenerator.genSuccessResult(indexConfig);
    }

    /**
     * 添加
     */

    @PostMapping("/indexConfig")
    @ApiOperation(value = "添加新配置", notes = "")
    public Result save(@RequestBody IndexConfig indexConfig,@TokenToAdminUser AdminUser loginAdminUser) {
        if (Objects.isNull(indexConfig.getConfigType())
                || StringUtils.isEmpty(indexConfig.getConfigName())
                || Objects.isNull(indexConfig.getConfigRank())
                || StringUtils.isEmpty(indexConfig.getGoodsId())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = simpleMallIndexConfigService.saveIndexConfig(indexConfig);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

/**
     * 修改
     */

    @PutMapping("/indexConfig")
    @ApiOperation(value = "修改商品信息", notes = "")
    public Result update(@RequestBody IndexConfig indexConfig,@TokenToAdminUser AdminUser loginAdminUser) {
        if (Objects.isNull(indexConfig.getConfigType())
                || Objects.isNull(indexConfig.getConfigId())
                || StringUtils.isEmpty(indexConfig.getConfigName())
                || Objects.isNull(indexConfig.getConfigRank())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = simpleMallIndexConfigService.updateIndexConfig(indexConfig);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


/**
     * 详情
     */

    @GetMapping("/indexConfig/{configId}")
    @ApiOperation(value = "根据configId查看配置详情", notes = "")
    public Result info(@PathVariable("configId") Long configId,@TokenToAdminUser AdminUser loginAdminUser) {
        IndexConfig config = simpleMallIndexConfigService.getIndexConfigById(configId);
        if (config == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(config);
    }


/**
     * 删除
     */

    @DeleteMapping("/indexConfig/{configId}")
    @ApiOperation(value = "删除商品", notes = "")
    public Result delete(@PathVariable Long configId,@TokenToAdminUser AdminUser loginAdminUser) {
        if (simpleMallIndexConfigService.deleteById(configId)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}
