package com.zhangshuo.autotest.aop.access;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserPower {
    //自定义权限值，如果多个权限，以逗号分割
    String power() default "";
}
