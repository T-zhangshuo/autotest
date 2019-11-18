package com.zhangshuo.autotest.appium;

import io.appium.java_client.AppiumDriver;

public interface IDoAppAction {

    Object doAction(AppiumDriver driver,AppAction appAction);
}
