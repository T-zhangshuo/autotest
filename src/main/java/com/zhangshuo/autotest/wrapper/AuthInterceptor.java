package com.zhangshuo.autotest.wrapper;

import com.zhangshuo.autotest.model.Token;
import com.zhangshuo.autotest.service.RedisService;
import com.zhangshuo.autotest.utils.DataConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI();
        if (StringUtils.isEmpty(requestUrl)) return false;
        String header = request.getHeader("User-Agent");
        redisService.sSet(DataConfig.REDIS_USERAGENT, header);
        //验证Wx
        String authorization = request.getHeader(DataConfig.TOKEN);
        if (!StringUtils.isEmpty(authorization)) {
            //取出md5的值
            List<Token> userList = redisService.getLike("*:" + authorization);
            if (userList != null && userList.size() > 0) {
                Token user = userList.get(userList.size() - 1);
                request.setAttribute(DataConfig.ATTRIBUTE_USER, user);
                return true;
            }
        }
        response.sendRedirect(DataConfig.AUTH_FAIL);
        return false;
    }
}
