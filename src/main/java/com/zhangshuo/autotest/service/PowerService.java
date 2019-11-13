package com.zhangshuo.autotest.service;

import com.zhangshuo.basebus.mapper.BasePowerMapper;
import com.zhangshuo.basebus.model.BasePower;
import com.zhangshuo.basebus.service.BasePowerService;

import java.util.List;

public interface PowerService extends BasePowerService {

    List<BasePower> findPowerByRoleId(String roleId);
}
