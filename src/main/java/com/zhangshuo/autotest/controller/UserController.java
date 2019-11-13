package com.zhangshuo.autotest.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.zhangshuo.autotest.aop.access.UserPower;
import com.zhangshuo.autotest.model.RestApi;
import com.zhangshuo.autotest.model.Token;
import com.zhangshuo.autotest.service.RedisService;
import com.zhangshuo.autotest.service.RoleService;
import com.zhangshuo.autotest.service.UserService;
import com.zhangshuo.autotest.utils.AutoIdUtils;
import com.zhangshuo.autotest.utils.DataConfig;
import com.zhangshuo.autotest.utils.TokenUtils;
import com.zhangshuo.basebus.model.BaseRole;
import com.zhangshuo.basebus.model.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/login.json", method = RequestMethod.POST)
    public RestApi<String> login(String username, String password) {
        RestApi<String> restApi = new RestApi<>();
        BaseUser loginUser = userService.login(username, password);
        if (loginUser == null) {
            restApi.setMsg("登录失败，用户信息不存在～");
            return restApi;
        }
        //根据用户id，查询角色
        BaseRole userRole = roleService.findRoleByUserId(loginUser.getId());
        Token token = new Token();
        token.setId(loginUser.getId());
        token.setRoleId(userRole.getId());
        token.setRoleCode(userRole.getCode());
        String md5Token = DigestUtils.md5DigestAsHex(AutoIdUtils.get().nextId().getBytes()).toUpperCase();
        //删除原有的旧的token
        redisService.delLike(loginUser.getId() + ":*");
        //保存新的token
        boolean updateRedisFlag = redisService.set(loginUser.getId() + ":" + md5Token, token, DataConfig.REDIS_TIME_USERSTATUS);
        if (updateRedisFlag)
            restApi.setData(md5Token);
        return restApi;
    }


    @UserPower(power = "register")
    @RequestMapping(value = "/register.json", method = RequestMethod.POST)
    public RestApi<Boolean> register(String userName, String password,
                                     @RequestParam(required = false) String nickName,
                                     @RequestParam(required = false) String realName,
                                     @RequestParam(required = false) String avatar,
                                     @RequestParam(required = false) Integer gender) {
        RestApi<Boolean> restApi = new RestApi<>();
        BaseUser register = userService.register(userName, password, nickName, realName, avatar, gender);
        if (register == null) {
            restApi.setMsg("创建失败，已存在相同用户名～");
            return restApi;
        }
        restApi.setData(true);
        return restApi;
    }

    @RequestMapping("/authFail.json")
    public RestApi<String> authFail() {
        RestApi<String> restApi = new RestApi<>();
        restApi.setOptCode(DataConfig.STATUS_USER_AUTH_FAIL);
        restApi.setMsg("您的授权已经失效了!");
        return restApi;
    }


}
