package com.zhangshuo.autotest.appium.actions;

import com.zhangshuo.autotest.appium.AppAction;
import com.zhangshuo.autotest.appium.IDoAppAction;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

public class FindElementByIdAction implements IDoAppAction {
    @Override
    public Object doAction(AppiumDriver driver, AppAction appAction) {
        WebElement elementById = driver.findElementById(appAction.getValue());
        return Actions.doWebElementAction(elementById, appAction.getAction());
    }
}
