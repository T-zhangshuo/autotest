package com.zhangshuo.autotest.service.impl;

import com.zhangshuo.autotest.service.DriverConfigService;
import com.zhangshuo.autotest.utils.AutoIdUtils;
import com.zhangshuo.basebus.mapper.BaseDriverConfigMapper;
import com.zhangshuo.basebus.model.BaseDriverConfig;
import com.zhangshuo.basebus.service.BaseDriverConfigService;
import com.zhangshuo.basebus.service.impl.BaseDriverConfigServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverConfigServiceImpl extends BaseDriverConfigServiceImpl implements DriverConfigService {
    @Autowired
    private BaseDriverConfigMapper baseDriverConfigMapper;

    @Override
    public boolean addDriverConfig(String userId, String name, String type, String version) {
        BaseDriverConfig driverConfig = new BaseDriverConfig();
        driverConfig.setId(AutoIdUtils.get().nextId());
        driverConfig.setUserId(userId);
        driverConfig.setDeviceName(name);
        driverConfig.setPlatformType(type);
        driverConfig.setPlatformVersion(version);
        return baseDriverConfigMapper.insert(driverConfig) > 0;
    }
}
