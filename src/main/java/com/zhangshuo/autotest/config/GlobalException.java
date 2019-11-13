package com.zhangshuo.autotest.config;

import com.zhangshuo.autotest.model.RestApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalException extends Throwable {

    private String msg;

    public GlobalException() {
    }

    public GlobalException(String msg) {
        this.msg = msg;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestApi exceptionHandler(Exception e) {
        log.error("发生异常: {}, {}", msg, e);
        RestApi restApi = new RestApi();
        restApi.setMsg(StringUtils.isEmpty(msg) ? "呀～服务器开小差啦～" : msg);
        return restApi;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public RestApi runtimeExceptionHandler(Exception e) {
        log.error("发生异常: {}, {}", msg, e);
        RestApi restApi = new RestApi();
        restApi.setMsg(StringUtils.isEmpty(msg) ? "呀～服务器开小差啦～" : msg);
        return restApi;
    }

}
