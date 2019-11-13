package com.zhangshuo.autotest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangshuo.autotest.mapper.PowerMapper;
import com.zhangshuo.autotest.mapper.RoleMapper;
import com.zhangshuo.autotest.service.RedisService;
import com.zhangshuo.autotest.service.RoleService;
import com.zhangshuo.autotest.utils.AutoIdUtils;
import com.zhangshuo.autotest.utils.DataConfig;
import com.zhangshuo.basebus.mapper.BaseRoleMapper;
import com.zhangshuo.basebus.mapper.BaseUserRoleMapper;
import com.zhangshuo.basebus.model.BasePower;
import com.zhangshuo.basebus.model.BaseRole;
import com.zhangshuo.basebus.model.BaseUser;
import com.zhangshuo.basebus.model.BaseUserRole;
import com.zhangshuo.basebus.service.impl.BaseRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends BaseRoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private BaseRoleMapper baseRoleMapper;
    @Autowired
    private BaseUserRoleMapper baseUserRoleMapper;
    @Autowired
    private PowerMapper powerMapper;
    @Autowired
    private RedisService redisService;


    @Override
    public BaseRole findRoleByUserId(String userId) {
        return roleMapper.findByUserId(userId);
    }

    @Override
    public List<BaseRole> findAllRole() {
        return roleMapper.findAllRole();
    }

    @Override
    public Boolean updateRoleRedis(BaseRole role) {
        List<BasePower> powerList = powerMapper.findPowerByRoleId(role.getId());
        if (powerList == null || powerList.size() == 0) return true;
        String key = DataConfig.REDIS_ROLE + role.getId();
        StringBuilder valueBuilder = new StringBuilder();
        for (BasePower power : powerList) {
            valueBuilder.append(power.getCode()).append(",");
        }
        String value = valueBuilder.toString();
        return redisService.set(key, value);
    }

    @Override
    public BaseRole addRole(String name, String code) {
        BaseRole baseRole = new BaseRole();
        baseRole.setId(AutoIdUtils.get().nextId());
        baseRole.setName(name);
        baseRole.setCode(code);
        int insert = baseRoleMapper.insert(baseRole);
        return insert > 0 ? baseRole : null;
    }

    @Override
    public boolean bindUserRole(String userId, String roleId) {
        //检测是否有这个关系
        QueryWrapper<BaseUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("role_id", roleId);
        Integer integer = baseUserRoleMapper.selectCount(queryWrapper);
        if (integer > 0) {
            return true;
        }
        BaseUserRole baseUserRole = new BaseUserRole();
        baseUserRole.setId(AutoIdUtils.get().nextId());
        baseUserRole.setUserId(userId);
        baseUserRole.setRoleId(roleId);
        return baseUserRoleMapper.insert(baseUserRole) > 0;
    }
}
