package com.zhangshuo.autotest.service.impl;

import com.zhangshuo.autotest.mapper.PowerMapper;
import com.zhangshuo.autotest.service.PowerService;
import com.zhangshuo.basebus.model.BasePower;
import com.zhangshuo.basebus.model.BaseRole;
import com.zhangshuo.basebus.service.BasePowerService;
import com.zhangshuo.basebus.service.impl.BasePowerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PowerServiceImpl extends BasePowerServiceImpl implements PowerService {

    @Autowired
    private PowerMapper wxPowerMapper;

    @Override
    public List<BasePower> findPowerByRoleId(String roleId) {
        return wxPowerMapper.findPowerByRoleId(roleId);
    }
}
