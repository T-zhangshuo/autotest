package com.zhangshuo.autotest.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.zhangshuo.autotest.model.RestApi;
import com.zhangshuo.autotest.model.Token;
import com.zhangshuo.autotest.service.DriverConfigService;
import com.zhangshuo.autotest.utils.DataConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/app/driver")
public class DriverController {

    @Autowired
    private DriverConfigService driverConfigService;

    @RequestMapping("/addDriver.json")
    public RestApi<Boolean> addDriver(String name, String type, String version, HttpServletRequest request) {
        RestApi<Boolean> restApi=new RestApi<>();
        Token userToken = DataConfig.getUserToken(request);
        boolean b = driverConfigService.addDriverConfig(userToken.getId(), name, type, version);
        return restApi;
    }

}
