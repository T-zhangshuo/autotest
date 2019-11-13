package com.zhangshuo.autotest.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class RestApi<T> {
    private boolean opt = false;
    private int optCode = -1;
    private String msg = "失败";
    private T data;

    public void setData(T data) {
        this.data = data;
        if (data != null) {
            opt = true;
            if (optCode == -1)
                optCode = 1;
            if ("失败".equals(msg))
                msg = "成功";
        } else {
            opt = false;
        }
    }

    public void setOpt(Boolean opt) {
        this.opt = opt;
        if (opt) {
            optCode = 1;
            msg = "成功";
            if (data == null)
                data = (T) opt;
        } else {
            optCode = -1;
            msg = "失败";
        }

    }
}