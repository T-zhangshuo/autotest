package com.zhangshuo.autotest.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.zhangshuo.autotest.model.RestApi;
import com.zhangshuo.autotest.model.Token;
import com.zhangshuo.autotest.service.DriverConfigService;
import com.zhangshuo.autotest.utils.DataConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/app/driver")
public class DriverController {

    @Autowired
    private DriverConfigService driverConfigService;

    @RequestMapping(value = "/add.json", method = RequestMethod.POST)
    public RestApi<Boolean> addDriver(String name, String type, String version,
                                      String serviceIp,
                                      HttpServletRequest request) {
        RestApi<Boolean> restApi = new RestApi<>();
        Token userToken = DataConfig.getUserToken(request);
        boolean b = driverConfigService.addDriverConfig(userToken.getId(), name, type, version, serviceIp);
        restApi.setData(b);
        return restApi;
    }

    @RequestMapping(value = "/delete.json", method = RequestMethod.POST)
    public RestApi<Boolean> delDriver(String id) {
        RestApi<Boolean> restApi = new RestApi<>();
        restApi.setData(driverConfigService.delDriverConfig(id));
        return restApi;
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    public RestApi<Boolean> updateDriver(String id,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) String type,
                                         @RequestParam(required = false) String version,
                                         @RequestParam(required = false) String serviceIp) {
        RestApi<Boolean> restApi = new RestApi<>();
        restApi.setData(driverConfigService.updateDriverConfig(id, name, type, version, serviceIp));
        return restApi;
    }
}
