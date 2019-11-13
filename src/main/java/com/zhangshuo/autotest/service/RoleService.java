package com.zhangshuo.autotest.service;

import com.zhangshuo.basebus.model.BaseRole;
import com.zhangshuo.basebus.service.BaseRoleService;

import java.util.List;

public interface RoleService extends BaseRoleService {

    BaseRole findRoleByUserId(String userId);

    List<BaseRole> findAllRole();

    Boolean updateRoleRedis(BaseRole role);

    BaseRole addRole(String name,String code);

    boolean bindUserRole(String userId,String roleId);
}
