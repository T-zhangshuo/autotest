package com.zhangshuo.autotest.mapper;


import com.zhangshuo.basebus.model.BasePower;

import java.util.List;

public interface PowerMapper {
    List<BasePower> findPowerByRoleId(String roleId);
}
