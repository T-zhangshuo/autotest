package com.zhangshuo.autotest.appium.actions;

import com.zhangshuo.autotest.appium.AppAction;
import com.zhangshuo.autotest.appium.IDoAppAction;
import com.zhangshuo.autotest.utils.StringUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;
import java.util.List;

public class Actions {

    public static Object doWebElementAction(WebElement webElement, String actions) {
        Object object = null;
        String action = actions;
        String value = "";
        if (actions.contains("(")) {
            action = actions.substring(0, actions.indexOf("("));
            value = actions.substring(actions.indexOf("(") + 1, actions.length() - 1);
        }
        switch (action) {
            case "click":
                webElement.click();
                break;
            case "submit":
                webElement.submit();
                break;
            case "sendKeys":
                webElement.sendKeys(value.split(","));
                break;
            case "clear":
                webElement.clear();
                break;
            case "getTagName":
                object = webElement.getTagName();
                break;
            case "getAttribute":
                object = webElement.getAttribute(value);
                break;
            case "isSelected":
                object = webElement.isSelected();
                break;
            case "isEnabled":
                object = webElement.isEnabled();
                break;
            case "getText":
                object = webElement.getText();
                break;
            case "findElement":
                By by = getBy(value);
                if (by != null) {
                    String childAction = getChildAction(value);
                    object = webElement.findElements(by);
                    WebElement webElement2 = (WebElement) object;
                    object = doWebElementAction(webElement2, childAction);
                }
                break;
            case "isDisplayed":
                object = webElement.isDisplayed();
                break;
            case "getLocation":
                object = webElement.getLocation();
                break;
            case "getSize":
                object = webElement.getSize();
                break;
            case "getRect":
                object = webElement.getRect();
                break;
            case "getCssValue":
                object = webElement.getCssValue(value);
                break;

        }
        return object;
    }

    private static By getBy(String value) {
        //格式 id:1111,findElementById|com.android.calculator2:id/digit_1|click
        String[] dataArray = value.split(",")[0].split(":");
        try {
            Method method = By.class.getMethod(dataArray[0], String.class);
            return (By) method.invoke(null, dataArray[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getChildAction(String value) {
        return value.split(",")[1];
    }


    public static Object doAction(AppiumDriver driver, AppAction appAction) throws Exception {
        String targetClassName = IDoAppAction.class.getPackage().getName()
                + ".actions."
                + StringUtils.firstUpperCase(appAction.getFindway())
                + "Action";
        Class targetClass = Class.forName(targetClassName);
        IDoAppAction iDoAppAction = (IDoAppAction) targetClass.newInstance();
        return iDoAppAction.doAction(driver, appAction);
    }
}
