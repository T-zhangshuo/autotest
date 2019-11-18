package com.zhangshuo.autotest.controller;

import com.zhangshuo.autotest.model.RestApi;
import com.zhangshuo.autotest.model.Token;
import com.zhangshuo.autotest.service.AppConfigService;
import com.zhangshuo.autotest.utils.DataConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api("APP配置")
@RequestMapping("/app/config")
@RestController
public class AppConfigController {

    @Autowired
    private AppConfigService appConfigService;

    @ApiOperation(value = "新增APP信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "APP名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "apppkg", value = "Android包名，IOS是APP的地址", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appact", value = "Android启动的Activity名称，IOS是UUID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appwaitact", value = "Android启动后等待Activity名称，IOS是测试工具名称", required = true, dataType = "String"),
    })
    @PostMapping(value = "/add.json")
    public RestApi<Boolean> add(String name, String apppkg, String appact, String appwaitact, HttpServletRequest request) {
        RestApi<Boolean> restApi = new RestApi<>();
        Token userToken = DataConfig.getUserToken(request);
        restApi.setData(appConfigService.addConfig(userToken.getId(), name, apppkg, appact, appwaitact));
        return restApi;
    }

    @RequestMapping(value = "/delete.json", method = RequestMethod.POST)
    public RestApi<Boolean> delete(String id) {
        RestApi<Boolean> restApi = new RestApi<>();
        restApi.setData(appConfigService.deleteConfig(id));
        return restApi;
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    public RestApi<Boolean> update(String id,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String apppkg,
                                   @RequestParam(required = false) String appact,
                                   @RequestParam(required = false) String appwaitact) {
        RestApi<Boolean> restApi = new RestApi<>();
        restApi.setData(appConfigService.updateConfig(id, name, apppkg, appact, appwaitact));
        return restApi;
    }

}
