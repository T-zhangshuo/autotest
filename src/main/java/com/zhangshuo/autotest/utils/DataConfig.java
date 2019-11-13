package com.zhangshuo.autotest.utils;


import com.zhangshuo.autotest.model.Token;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class DataConfig {
    //用户失效24小时*7天
    public static final long REDIS_TIME_USERSTATUS = 7 * 24 * 3600 * 1000;

    public static final String TOKEN = "token";

    public static final List<String> IGNORES_URL = Arrays.asList("/user/login.json", "/user/authFail.json");
    public static final String AUTH_FAIL="/user/authFail.json";
    public static final String ATTRIBUTE_USER = "AUser";
    public static final String REDIS_USERAGENT = "UserAgent";
    public static final String REDIS_ROLE = "Role:";


    //管理员失败码
    public static final String ADMIN_CODE = "admin";

    //登录失败的授权码
    public static final int STATUS_USER_AUTH_FAIL = 10002;

    public static Token getUserToken(HttpServletRequest request) {
        Object attribute = request.getAttribute(DataConfig.ATTRIBUTE_USER);
        return (Token) attribute;
    }
}
