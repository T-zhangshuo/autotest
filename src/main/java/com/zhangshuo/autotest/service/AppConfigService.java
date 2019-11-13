package com.zhangshuo.autotest.service;

import com.zhangshuo.basebus.service.BaseAppConfigService;

public interface AppConfigService extends BaseAppConfigService {
    Boolean addConfig(String userId,String name, String apppkg, String appact, String appwaitact);

    Boolean deleteConfig(String id);

    Boolean updateConfig(String id, String name, String apppkg, String appact, String appwaitact);
}
