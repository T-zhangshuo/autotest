package com.zhangshuo.autotest.controller;

import com.zhangshuo.autotest.appium.AppAction;
import com.zhangshuo.autotest.appium.AppManager;
import com.zhangshuo.autotest.appium.IDoAppAction;
import com.zhangshuo.autotest.appium.actions.Actions;
import com.zhangshuo.autotest.enums.EDriverType;
import com.zhangshuo.autotest.model.RestApi;
import com.zhangshuo.autotest.service.AppConfigService;
import com.zhangshuo.autotest.service.DriverConfigService;
import com.zhangshuo.autotest.service.TaskCaseService;
import com.zhangshuo.autotest.service.WebSocketServer;
import com.zhangshuo.autotest.utils.StringUtils;
import com.zhangshuo.basebus.model.BaseAppConfig;
import com.zhangshuo.basebus.model.BaseDriverConfig;
import com.zhangshuo.basebus.model.BaseTaskCase;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.AnnotationUtils;
import org.apache.http.util.TextUtils;
import org.openqa.selenium.WebDriver;
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
    @Autowired
    private WebSocketServer webSocketServer;


    @RequestMapping("/connectTest.json")
    public RestApi<String> connectTest(String driverId, String appId) {
        RestApi<String> restApi = new RestApi<>();
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

        String url = "http://" + driverConfig.getServiceIp() + "/wd/hub";
        try {
            String driverType = driverConfig.getPlatformType();
            if (driverType.equalsIgnoreCase(EDriverType.Android.toString())) {
                AndroidDriver driver = new AndroidDriver(new URL(url), appManager.getAndroidCap(driverConfig, appConfig));
                appManager.addDriver(driverId, driver);
            } else if (driverType.equalsIgnoreCase(EDriverType.IOS.toString())) {
                IOSDriver driver = new IOSDriver(new URL(url), appManager.getIosCap(driverConfig, appConfig));
                appManager.addDriver(driverId, driver);
            }
            restApi.setData("连接成功～");
        } catch (Exception e) {
            restApi.setMsg("连接失败 \n>>: 远程地址: " + url + " \n>>: 错误信息: " + e.getLocalizedMessage());
        }
        return restApi;
    }

    @RequestMapping("/excuteCase.json")
    public RestApi<String> excuteCase(String driverId, String appId, String caseId) throws Exception {
        webSocketServer.sendMessage(driverId, "开始连接设备～");
        RestApi<String> restApi = connectTest(driverId, appId);
        if (!restApi.isOpt()) {
            return restApi;
        }
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
        String[] cmdArray = cmds.split(",");
        if (cmdArray.length == 0) {
            restApi.setMsg("执行失败～ 该测试用例无执行脚本");
            return restApi;
        }
        List<AppAction> appActionList = new ArrayList<>();
        webSocketServer.sendMessage(driverId, "开始解析测试任务～");
        for (int i = 0; i < cmdArray.length; i++) {
            String cmd = cmdArray[i];
            webSocketServer.sendMessage(driverId, " 正在解析任务(" + (i + 1) + "/" + cmdArray.length + "):  " + cmd);
            String[] cmdtmp = cmd.split("\\|");
            if (cmdtmp.length != 3) {
                webSocketServer.sendMessage(driverId, "错误: 任务格式未满足要求～ (" + cmd + ")");
                restApi.setMsg("执行失败～ 该测试用例存在格式错误的任务!");
                return restApi;
            }
            AppAction appAction = new AppAction();
            appAction.setFindway(cmdtmp[0]);
            appAction.setValue(cmdtmp[1]);
            appAction.setAction(cmdtmp[2]);
            appActionList.add(appAction);
        }
        webSocketServer.sendMessage(driverId, "开始执行测试任务～");
        if (appActionList.size() > 0) {
            AppiumDriver driver = (AppiumDriver) appManager.getDriver(driverId);
            if (driver == null) {
                restApi.setMsg("执行失败～ 未找到已连接设备");
                return restApi;
            }
            for (AppAction appAction : appActionList) {
                webSocketServer.sendMessage(driverId, "正在执行任务:  " + appAction.toString());
                try {
                    Object result = Actions.doAction(driver, appAction);
                    webSocketServer.sendMessage(driverId, "执行结束:  已完成任务! " + (result != null ? result.toString() : ""));
                } catch (Exception e) {
                    webSocketServer.sendMessage(driverId, "执行结束:  任务失败～");
                    break;
                }
            }

        }
        return restApi;
    }
}
