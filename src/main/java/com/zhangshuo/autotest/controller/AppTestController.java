package com.zhangshuo.autotest.controller;

import com.zhangshuo.autotest.appium.AppAction;
import com.zhangshuo.autotest.appium.AppManager;
import com.zhangshuo.autotest.model.RestApi;
import com.zhangshuo.autotest.service.AppConfigService;
import com.zhangshuo.autotest.service.DriverConfigService;
import com.zhangshuo.autotest.service.TaskCaseService;
import com.zhangshuo.basebus.model.BaseAppConfig;
import com.zhangshuo.basebus.model.BaseDriverConfig;
import com.zhangshuo.basebus.model.BaseTaskCase;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.AnnotationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/app/test")
@RestController
public class AppTestController {

    @Autowired
    private AppManager appManager;
    @Autowired
    private DriverConfigService driverConfigService;
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private TaskCaseService taskCaseService;

    @RequestMapping("/connect.json")
    public RestApi<String> connect(String driverId, String appId, String type) {
        RestApi<String> restApi = new RestApi<>();
        if (!"Android".equalsIgnoreCase(type) &&
                !"IOS".equalsIgnoreCase(type)) {
            restApi.setMsg("连接类型错误～");
            return restApi;
        }

        BaseDriverConfig driverConfig = driverConfigService.getById(driverId);
        BaseAppConfig appConfig = appConfigService.getById(appId);
        if (driverConfig == null) {
            restApi.setMsg("设备信息不存在～");
            return restApi;
        }
        if (appConfig == null) {
            restApi.setMsg("APP信息不存在～");
            return restApi;
        }

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", type); //指定测试平台
        cap.setCapability("deviceName", driverConfig.getDeviceName()); //指定测试机的ID,通过adb命令`adb devices`获取
        cap.setCapability("platformVersion", driverConfig.getPlatformVersion());

        cap.setCapability("appPackage", appConfig.getAppPackage());
        cap.setCapability("appActivity", appConfig.getAppActivity());

        cap.setCapability("appWaitActivity", appConfig.getAppWaitactivity());
        //每次启动时覆盖session，否则第二次后运行会报错不能新建session
        cap.setCapability("sessionOverride", true);
        String url = "http://" + driverConfig.getServiceIp() + "/wd/hub";
        try {
            AndroidDriver driver = new AndroidDriver(new URL(url), cap);
            appManager.addDriver(driverId, driver);
            restApi.setData("连接成功～");
        } catch (Exception e) {
            restApi.setMsg("连接失败 <br/>>>: 配置信息: " + cap.toString() + " <br/>>>: 远程地址: " + url + " <br/>>>: 错误信息: " + e.getLocalizedMessage());
        }
        return restApi;
    }

    @RequestMapping("/excuteCase.json")
    public RestApi<String> excuteCase(String caseId, String driverId) {
        RestApi<String> restApi = new RestApi<>();
        BaseTaskCase taskCase = taskCaseService.getById(caseId);
        if (taskCase == null) {
            restApi.setMsg("执行失败～ 未找到测试用例");
            return restApi;
        }
        String cmds = taskCase.getCmds();
        if (TextUtils.isEmpty(cmds)) {
            restApi.setMsg("执行失败～ 该测试用例无执行脚本");
            return restApi;
        }
        //[findElementById](com.android.calculator2:id/digit1)[click]()
        String[] cmdArray = cmds.split(",");
        if (cmdArray.length == 0) {
            restApi.setMsg("执行失败～ 该测试用例无执行脚本");
            return restApi;
        }
        List<AppAction> appActionList = new ArrayList<>();
        StringBuilder cmdTipBuilder = new StringBuilder();
        cmdTipBuilder.append("<br/>命令提示: " + cmds);
        for (int i = 0; i < cmdArray.length; i++) {
            String cmd = cmdArray[i];
            String[] cmdtmp = cmd.split("\\|");
            if (cmdtmp.length != 3) {
                cmdTipBuilder.append("<br/>错误: " + cmd + " 命令格式未满足要求!忽略此命令～");
                continue;
            }
            if ("findElements".equalsIgnoreCase(cmdtmp[0])) {
                cmdTipBuilder.append("<br/>提示: " + cmd + " 命令不支持!忽略此命令～");
                continue;
            }
            AppAction appAction = new AppAction();
            appAction.setFindway(cmdtmp[0]);
            appAction.setValue(cmdtmp[1]);
            appAction.setAction(cmdtmp[2]);
            appActionList.add(appAction);
        }
        if (appActionList.size() > 0) {
            AppiumDriver driver = appManager.getDriver(driverId);
            if(driver==null){
                restApi.setMsg("未找到已连接设备～");
                return restApi;
            }
            for (AppAction appAction : appActionList) {
                //TODO 待优化
                cmdTipBuilder.append("<br/>开始执行: " + appAction.getFindway() + " | " + appAction.getValue() + " | " + appAction.getAction());
                switch (appAction.getFindway()) {
                    case "findElementById":
                        WebElement elementById = driver.findElementById(appAction.getAction());
                        switch (appAction.getAction()) {
                            case "click":
                                elementById.click();
                                cmdTipBuilder.append("<br/>结束执行: 成功");
                                break;
                        }
                        break;
                }
                cmdTipBuilder.append("<br/>结束执行: 失败");
            }
        } else {
            cmdTipBuilder.append("<br/>无满足命令条件～");
        }
        restApi.setData(cmdTipBuilder.toString());
        return restApi;
    }
}
