package com.zhangshuo.autotest.appium;

import io.appium.java_client.AppiumDriver;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * 统一的APP设备管理类
 *
 * @author zhangshuo
 */
@Component
public class AppManager {
    private HashMap<String, AppiumDriver> driverMap = new HashMap<>();

    /**
     * 添加设备
     *
     * @param driverId
     * @param driver
     */
    public void addDriver(@Nonnull String driverId, @Nonnull AppiumDriver driver) {
        removeDriver(driverId);
        driverMap.put(driverId, driver);
    }


    /**
     * 删除drvier
     *
     * @param driverId
     */
    public void removeDriver(@Nonnull String driverId) {
        AppiumDriver appiumDriver = driverMap.get(driverId);
        if (appiumDriver != null) {
            driverMap.remove(driverId);
        }
    }

    /**
     * 获取driver
     *
     * @param driverId
     * @return
     */
    @Nullable
    public AppiumDriver getDriver(@Nonnull String driverId) {
        return driverMap.get(driverId);
    }

}
