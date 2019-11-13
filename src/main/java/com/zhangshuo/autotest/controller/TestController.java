package com.zhangshuo.autotest.controller;


import com.zhangshuo.autotest.appium.AppManager;
import com.zhangshuo.autotest.model.RestApi;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;


@RequestMapping("/app")
@RestController
public class TestController {

    @Autowired
    private AppManager appManager;

    @RequestMapping(value = "/add.json",method = RequestMethod.GET)
    public RestApi<Boolean> add(String driverId){
        RestApi<Boolean> restApi=new RestApi<>();

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.BROWSER_NAME, "");
        cap.setCapability("platformName", "Android"); //指定测试平台
        cap.setCapability("deviceName", "c708dc6c"); //指定测试机的ID,通过adb命令`adb devices`获取
        cap.setCapability("platformVersion", "8.1.0");

        //将上面获取到的包名和Activity名设置为值
        cap.setCapability("appPackage", "com.android.calculator2");
        cap.setCapability("appActivity", "com.android.calculator2.Calculator");

        //A new session could not be created的解决方法
        cap.setCapability("appWaitActivity","com.android.calculator2.Calculator");
        //每次启动时覆盖session，否则第二次后运行会报错不能新建session
        cap.setCapability("sessionOverride", true);

        try {
            AndroidDriver driver = new AndroidDriver(new URL("http://192.168.0.110:4723/wd/hub"), cap);
            appManager.addDriver(driverId,driver);
            restApi.setData(true);
        } catch (MalformedURLException e) {
            restApi.setOpt(false);
        }
        return restApi;
    }

    @RequestMapping(value = "/test.json",method = RequestMethod.GET)
    public RestApi<Boolean> test(String driverId){
        RestApi<Boolean> restApi=new RestApi<>();
        AppiumDriver driver = appManager.getDriver(driverId);
        if(driver==null){
            return restApi;
        }
        //获取1
        driver.findElementById("com.android.calculator2:id/digit1").click();
        //获取+
        driver.findElementById("com.android.calculator2:id/plus").click();
        //获取2
        driver.findElementById("com.android.calculator2:id/digit2").click();
        //获取=
        driver.findElementById("com.android.calculator2:id/equal").click();
        restApi.setData(true);
        return restApi;
    }
}
