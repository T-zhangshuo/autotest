package com.zhangshuo.autotest.controller;

import com.zhangshuo.autotest.model.RestApi;
import com.zhangshuo.autotest.model.Token;
import com.zhangshuo.autotest.service.TaskCaseService;
import com.zhangshuo.autotest.service.TaskService;
import com.zhangshuo.autotest.utils.DataConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/task")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskCaseService taskCaseService;

    @RequestMapping(value = "/add.json",method = RequestMethod.POST)
    public RestApi<Boolean> addTask(String name, @RequestParam(required = false) String desc,
                                    HttpServletRequest request) {
        RestApi<Boolean> restApi = new RestApi<>();
        Token userToken = DataConfig.getUserToken(request);
        restApi.setData(taskService.add(userToken.getId(), name, desc));
        return restApi;
    }

    @RequestMapping(value = "/delete.json",method = RequestMethod.POST)
    public RestApi<Boolean> deleteTask(String id) {
        RestApi<Boolean> restApi = new RestApi<>();
        restApi.setData(taskService.delete(id));
        return restApi;
    }

    @RequestMapping(value = "/update.json",method = RequestMethod.POST)
    public RestApi<Boolean> updateTask(String id, @RequestParam(required = false) String name, @RequestParam(required = false) String desc) {
        RestApi<Boolean> restApi = new RestApi<>();
        restApi.setData(taskService.update(id, name, desc));
        return restApi;
    }

    @RequestMapping(value = "/case/add.json",method = RequestMethod.POST)
    public RestApi<Boolean> addTaskCase(String taskId, String name, String value,
                                        @RequestParam(required = false) String cmds,
                                        @RequestParam(required = false) String desc) {
        RestApi<Boolean> restApi = new RestApi<>();
        restApi.setData(taskCaseService.add(taskId, name, value, cmds, desc));
        return restApi;
    }

    @RequestMapping(value = "/case/delete.json",method = RequestMethod.POST)
    public RestApi<Boolean> deleteTaskCase(String id) {
        RestApi<Boolean> restApi = new RestApi<>();
        restApi.setData(taskCaseService.delete(id));
        return restApi;
    }

    @RequestMapping(value = "/case/update.json",method = RequestMethod.POST)
    public RestApi<Boolean> updateTaskCase(String id, String name, String value,
                                           @RequestParam(required = false) String cmds,
                                           @RequestParam(required = false) String desc) {
        RestApi<Boolean> restApi = new RestApi<>();
        restApi.setData(taskCaseService.update(id, name, value, cmds, desc));
        return restApi;
    }
}
