package com.zhangshuo.autotest.service.impl;

import com.zhangshuo.autotest.service.AppConfigService;
import com.zhangshuo.autotest.utils.AutoIdUtils;
import com.zhangshuo.basebus.mapper.BaseAppConfigMapper;
import com.zhangshuo.basebus.model.BaseAppConfig;
import com.zhangshuo.basebus.service.impl.BaseAppConfigServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppConfigServiceImpl extends BaseAppConfigServiceImpl implements AppConfigService {

    @Autowired
    private BaseAppConfigMapper baseAppConfigMapper;

    @Override
    public Boolean addConfig(String userId,String name, String apppkg, String appact, String appwaitact) {
        BaseAppConfig baseAppConfig = new BaseAppConfig();
        baseAppConfig.setId(AutoIdUtils.get().nextId());
        baseAppConfig.setUserId(userId);
        baseAppConfig.setName(name);
        baseAppConfig.setAppPackage(apppkg);
        baseAppConfig.setAppActivity(appact);
        baseAppConfig.setAppWaitactivity(appwaitact);
        return baseAppConfigMapper.insert(baseAppConfig) > 0;
    }

    @Override
    public Boolean deleteConfig(String id) {
        return baseAppConfigMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean updateConfig(String id, String name, String apppkg, String appact, String appwaitact) {
        BaseAppConfig baseAppConfig = new BaseAppConfig();
        baseAppConfig.setId(id);
        baseAppConfig.setName(name);
        baseAppConfig.setAppPackage(apppkg);
        baseAppConfig.setAppActivity(appact);
        baseAppConfig.setAppWaitactivity(appwaitact);
        return baseAppConfigMapper.updateById(baseAppConfig) > 0;
    }
}
