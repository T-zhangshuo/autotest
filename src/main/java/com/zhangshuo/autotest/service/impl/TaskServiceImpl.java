package com.zhangshuo.autotest.service.impl;

import com.zhangshuo.autotest.service.TaskService;
import com.zhangshuo.autotest.utils.AutoIdUtils;
import com.zhangshuo.basebus.mapper.BaseTaskMapper;
import com.zhangshuo.basebus.model.BaseTask;
import com.zhangshuo.basebus.service.impl.BaseTaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends BaseTaskServiceImpl implements TaskService {
    @Autowired
    private BaseTaskMapper taskMapper;

    @Override
    public Boolean add(String userId, String name, String desc) {
        BaseTask baseTask = new BaseTask();
        baseTask.setId(AutoIdUtils.get().nextId());
        baseTask.setName(name);
        baseTask.setRemarks(desc);
        baseTask.setUserId(userId);
        return taskMapper.insert(baseTask) > 0;
    }

    @Override
    public Boolean delete(String id) {
        return taskMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean update(String id, String name, String desc) {
        BaseTask baseTask = new BaseTask();
        baseTask.setId(id);
        baseTask.setName(name);
        baseTask.setRemarks(desc);
        return taskMapper.updateById(baseTask) > 0;
    }
}
