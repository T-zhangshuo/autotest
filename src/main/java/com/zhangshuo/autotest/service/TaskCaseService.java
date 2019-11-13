package com.zhangshuo.autotest.service;


import com.zhangshuo.basebus.service.BaseTaskCaseService;

public interface TaskCaseService extends BaseTaskCaseService {

    Boolean add(String taskId,String name,String value,String cmds,String desc);

    Boolean delete(String id);

    Boolean update(String id,String name,String value,String cmds,String desc);
}
