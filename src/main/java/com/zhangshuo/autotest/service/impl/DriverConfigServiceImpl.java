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
    public boolean addDriverConfig(String userId, String name, String type, String version,String serviceIp) {
        BaseDriverConfig driverConfig = new BaseDriverConfig();
        driverConfig.setId(AutoIdUtils.get().nextId());
        driverConfig.setUserId(userId);
        driverConfig.setDeviceName(name);
        driverConfig.setPlatformType(type);
        driverConfig.setPlatformVersion(version);
        driverConfig.setServiceIp(serviceIp);
        return baseDriverConfigMapper.insert(driverConfig) > 0;
    }

    @Override
    public boolean delDriverConfig(String id) {
        return baseDriverConfigMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateDriverConfig(String id, String name, String type, String version,String serviceIp) {
        BaseDriverConfig driverConfig = new BaseDriverConfig();
        driverConfig.setId(id);
        driverConfig.setDeviceName(name);
        driverConfig.setPlatformType(type);
        driverConfig.setPlatformVersion(version);
        driverConfig.setServiceIp(serviceIp);
        return baseDriverConfigMapper.updateById(driverConfig) > 0;
    }
}
