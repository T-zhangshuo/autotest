package com.zhangshuo.autotest.model;

import com.zhangshuo.basebus.model.BaseRole;
import lombok.Data;

@Data
public class Token {
    private String id;
    private String roleId;
    private String roleCode;
}
