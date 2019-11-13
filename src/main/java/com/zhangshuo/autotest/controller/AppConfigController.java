package com.zhangshuo.autotest.controller;

import com.zhangshuo.autotest.model.RestApi;
import com.zhangshuo.autotest.model.Token;
import com.zhangshuo.autotest.service.AppConfigService;
import com.zhangshuo.autotest.utils.DataConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/app/config")
@RestController
public class AppConfigController {

    @Autowired
    private AppConfigService appConfigService;

    @RequestMapping(value = "/add.json", method = RequestMethod.POST)
    public RestApi<Boolean> add(String name, String apppkg, String appact, String appwaitact, HttpServletRequest request) {
        RestApi<Boolean> restApi = new RestApi<>();
        Token userToken = DataConfig.getUserToken(request);
        restApi.setData(appConfigService.addConfig(userToken.getId(),name, apppkg, appact, appwaitact));
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
