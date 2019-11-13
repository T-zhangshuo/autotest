package com.zhangshuo.autotest.mapper;

import com.zhangshuo.basebus.model.BaseRole;

import java.util.List;

public interface RoleMapper {
    BaseRole findByUserId(String userId);

    List<BaseRole> findAllRole();
}
