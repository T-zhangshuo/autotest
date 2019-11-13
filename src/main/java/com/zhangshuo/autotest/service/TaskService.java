package com.zhangshuo.autotest.service;

import com.zhangshuo.basebus.service.BaseTaskService;

public interface TaskService extends BaseTaskService {

    Boolean add(String userId, String name, String desc);

    Boolean delete(String id);

    Boolean update(String id, String name, String desc);
}
