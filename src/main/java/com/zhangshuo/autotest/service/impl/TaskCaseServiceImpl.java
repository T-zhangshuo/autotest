package com.zhangshuo.autotest.service.impl;

import com.zhangshuo.autotest.service.TaskCaseService;
import com.zhangshuo.autotest.service.TaskService;
import com.zhangshuo.autotest.utils.AutoIdUtils;
import com.zhangshuo.basebus.mapper.BaseTaskCaseMapper;
import com.zhangshuo.basebus.mapper.BaseTaskMapper;
import com.zhangshuo.basebus.model.BaseTaskCase;
import com.zhangshuo.basebus.service.BaseTaskCaseService;
import com.zhangshuo.basebus.service.impl.BaseTaskCaseServiceImpl;
import com.zhangshuo.basebus.service.impl.BaseTaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskCaseServiceImpl extends BaseTaskCaseServiceImpl implements TaskCaseService {
    @Autowired
    private BaseTaskCaseMapper baseTaskCaseMapper;

    @Override
    public Boolean add(String taskId, String name, String value, String cmds, String desc) {
        BaseTaskCase baseTaskCase = new BaseTaskCase();
        baseTaskCase.setId(AutoIdUtils.get().nextId());
        baseTaskCase.setTaskId(taskId);
        baseTaskCase.setName(name);
        baseTaskCase.setValue(value);
        baseTaskCase.setCmds(cmds);
        baseTaskCase.setRemarks(desc);
        return baseTaskCaseMapper.insert(baseTaskCase) > 0;
    }

    @Override
    public Boolean delete(String id) {
        return baseTaskCaseMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean update(String id, String name, String value, String cmds, String desc) {
        BaseTaskCase baseTaskCase = new BaseTaskCase();
        baseTaskCase.setId(id);
        baseTaskCase.setName(name);
        baseTaskCase.setValue(value);
        baseTaskCase.setCmds(cmds);
        baseTaskCase.setRemarks(desc);
        return baseTaskCaseMapper.updateById(baseTaskCase) > 0;
    }
}
