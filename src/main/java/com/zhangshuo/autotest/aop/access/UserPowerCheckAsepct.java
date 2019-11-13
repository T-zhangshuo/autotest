package com.zhangshuo.autotest.aop.access;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zhangshuo.autotest.model.RestApi;
import com.zhangshuo.autotest.model.Token;
import com.zhangshuo.autotest.service.RedisService;
import com.zhangshuo.autotest.utils.DataConfig;
import com.zhangshuo.basebus.model.BaseRole;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Slf4j
@Component
public class UserPowerCheckAsepct {

    @Autowired
    private RedisService redisService;

    @Pointcut(value = "@annotation(com.zhangshuo.autotest.aop.access.UserPower)")
    private void userPowerCheckCut() {
    }

    @Around("userPowerCheckCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        if (targetMethod.isAnnotationPresent(UserPower.class)) {
            //获取方法上注解中表明的权限
            UserPower userPower = targetMethod.getAnnotation(UserPower.class);
            String power = userPower.power();
            if (StringUtils.isNotEmpty(power)) {
                String className = joinPoint.getTarget().getClass().getSimpleName();
                String methodName = signature.getName();
                log.info("当前 className:[ {} ],methodName:[ {} ] 访问权限为:[ {} ]", className, methodName, power);
                String[] powerArray = power.split(",");//接口允许的角色
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                if (requestAttributes == null) {
                    RestApi restApi = new RestApi();
                    restApi.setMsg("暂无访问权限哦～");
                    log.info("权限检验失败");
                    return restApi;
                }
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                Token userToken = DataConfig.getUserToken(request);
                if (userToken == null || StringUtils.isEmpty(userToken.getRoleId())) {
                    RestApi restApi = new RestApi();
                    restApi.setMsg("暂无访问权限哦～");
                    log.info("权限检验失败,原因: 用户未登录");
                    return restApi;
                }
                //不是管理员的情况下
                if (!DataConfig.ADMIN_CODE.equalsIgnoreCase(userToken.getRoleCode())) {
                    String allPower = (String) redisService.get(DataConfig.REDIS_ROLE + userToken.getRoleId());
                    String[] allPowerArray = allPower.split(",");

                    List<String> powerList = Arrays.asList(powerArray);
                    List<String> allPowerList = Arrays.asList(allPowerArray);
                    allPowerList.retainAll(powerList);
                    if (powerList.size() != allPowerList.size()) {
                        RestApi restApi = new RestApi();
                        restApi.setMsg("暂无访问权限哦～");
                        log.info("权限检验失败,原因: 用户暂无权限。[ {} ]", power);
                        return restApi;
                    }
                }
            }
        }
        return joinPoint.proceed();
    }
}
