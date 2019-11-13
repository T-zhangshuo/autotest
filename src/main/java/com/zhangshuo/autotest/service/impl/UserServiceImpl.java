package com.zhangshuo.autotest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhangshuo.autotest.service.UserService;
import com.zhangshuo.autotest.utils.AutoIdUtils;
import com.zhangshuo.basebus.mapper.BaseUserMapper;
import com.zhangshuo.basebus.model.BaseUser;
import com.zhangshuo.basebus.service.BaseUserService;
import com.zhangshuo.basebus.service.impl.BaseUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import sun.security.provider.MD5;

@Service
public class UserServiceImpl extends BaseUserServiceImpl implements UserService {
    @Autowired
    private BaseUserMapper baseUserMapper;

    @Override
    public BaseUser login(String username, String password) {
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase());
        queryWrapper.eq("status", 1);
        return baseUserMapper.selectOne(queryWrapper);
    }

    @Override
    public BaseUser register(String username, String password, String nickname, String realname, String avatar, Integer gender) {
        //检测是否已经有相同用户名的用户
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("status", 1);
        Integer integer = baseUserMapper.selectCount(queryWrapper);
        if (integer > 0) {
            return null;
        }
        BaseUser baseUser = new BaseUser();
        baseUser.setId(AutoIdUtils.get().nextId());
        baseUser.setUsername(username);
        baseUser.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase());
        baseUser.setNickname(nickname);
        baseUser.setRealname(realname);
        baseUser.setAvatar(avatar);
        baseUser.setGender(gender);
        return baseUserMapper.insert(baseUser) > 0?baseUser:null;
    }
}
