package com.zhangshuo.autotest.service;

import com.zhangshuo.basebus.model.BaseUser;
import com.zhangshuo.basebus.service.BaseUserService;

public interface UserService extends BaseUserService {
    BaseUser login(String username, String password);

    BaseUser register(String username,String password,String nickname,String realname,String avatar,Integer gender);
}
