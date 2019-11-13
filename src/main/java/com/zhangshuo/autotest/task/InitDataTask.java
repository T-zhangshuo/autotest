package com.zhangshuo.autotest.task;

import com.zhangshuo.autotest.service.RedisService;
import com.zhangshuo.autotest.service.RoleService;
import com.zhangshuo.autotest.service.UserService;
import com.zhangshuo.autotest.utils.DataConfig;
import com.zhangshuo.basebus.model.BaseRole;
import com.zhangshuo.basebus.model.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Order(1)
@Component
public class InitDataTask implements ApplicationRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initAdminAccount();
        initRole2Redis();

        log.info("初始化完成～～");
    }

    private void initAdminAccount() {
        log.info("START: 初始化管理员账号信息(不存在的情况下创建)");
        BaseUser register = userService.register(environment.getProperty("admin.username"),
                environment.getProperty("admin.password"), environment.getProperty("admin.nickname"), "", "", 1);
        if (register != null) {
            BaseRole baseRole = roleService.addRole("管理员", "admin");
            roleService.bindUserRole(register.getId(), baseRole.getId());
        }
        log.info("END: " + (register == null ? "已存在管理员账号，忽略" : "创建admin/admin账号成功"));
    }

    private void initRole2Redis() {
        log.info("START: 初始化导入角色、权限到Redis");
        //清除过期的权限
        redisService.delLike(DataConfig.REDIS_ROLE + "*");
        List<BaseRole> allRoleList = roleService.findAllRole();
        for (int i = 0; i < allRoleList.size(); i++) {
            BaseRole role = allRoleList.get(i);
            roleService.updateRoleRedis(role);
        }
        log.info("END: 初始化导入角色、权限到Redis");
    }
}
