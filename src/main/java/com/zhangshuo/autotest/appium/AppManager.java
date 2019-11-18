package com.zhangshuo.autotest.appium;

import com.zhangshuo.autotest.enums.EDriverType;
import com.zhangshuo.autotest.service.WebSocketServer;
import com.zhangshuo.basebus.model.BaseAppConfig;
import com.zhangshuo.basebus.model.BaseDriverConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一的APP设备管理类
 *
 * @author zhangshuo
 */
@Component
public class AppManager {

    private Map<String, AppiumDriver> appDriverMap = new ConcurrentHashMap<>();

    /**
     * 添加设备
     *
     * @param driverId
     * @param driver
     */
    public void addDriver(@Nonnull String driverId, @Nonnull AppiumDriver driver) {
        removeDriver(driverId);
        appDriverMap.put(driverId,driver);
    }


    /**
     * 删除drvier
     *
     * @param driverId
     */
    public void removeDriver(@Nonnull String driverId) {
        appDriverMap.remove(driverId);
    }

    /**
     * 获取driver
     *
     * @param driverId
     * @return
     */
    @Nullable
    public WebDriver getDriver(@Nonnull String driverId) {
        return appDriverMap.get(driverId);
    }

    public DesiredCapabilities getAndroidCap(BaseDriverConfig driverConfig, BaseAppConfig appConfig){
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", driverConfig.getPlatformType()); //指定测试平台
        cap.setCapability("deviceName", driverConfig.getDeviceName()); //指定测试机的ID,通过adb命令`adb devices`获取
        cap.setCapability("platformVersion", driverConfig.getPlatformVersion());
        cap.setCapability("appPackage", appConfig.getAppPackage());
        cap.setCapability("appActivity", appConfig.getAppActivity());
        cap.setCapability("appWaitActivity", appConfig.getAppWaitactivity());
        cap.setCapability("sessionOverride", true);
        return cap;
    }

    public DesiredCapabilities getIosCap(BaseDriverConfig driverConfig, BaseAppConfig appConfig){
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", driverConfig.getPlatformType()); //指定测试平台
        cap.setCapability("deviceName", driverConfig.getDeviceName()); //指定测试机的ID,通过adb命令`adb devices`获取
        cap.setCapability("platformVersion", driverConfig.getPlatformVersion());
        cap.setCapability("app", appConfig.getAppPackage());
        cap.setCapability("udid", appConfig.getAppActivity());
        cap.setCapability("antomationName", appConfig.getAppWaitactivity());
        cap.setCapability("noReset",true);
        cap.setCapability("sessionOverride", true);
        return cap;
    }
}
