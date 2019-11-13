package com.zhangshuo.autotest.service;

import com.zhangshuo.basebus.service.BaseDriverConfigService;

public interface DriverConfigService extends BaseDriverConfigService {
    boolean addDriverConfig(String userId, String name, String type, String version);
}
