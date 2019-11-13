package com.zhangshuo.basebus.service.impl;

import com.zhangshuo.basebus.model.BaseUser;
import com.zhangshuo.basebus.mapper.BaseUserMapper;
import com.zhangshuo.basebus.service.BaseUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangshuo
 * @since 2019-11-13
 */
@Service
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, BaseUser> implements BaseUserService {

}
