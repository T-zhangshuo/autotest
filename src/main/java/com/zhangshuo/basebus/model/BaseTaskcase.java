package com.zhangshuo.basebus.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangshuo
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task_case")
public class BaseTaskCase extends Model<BaseTaskCase> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private Integer status;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String taskId;

    private String name;

    private String value;

    private String cmds;

    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
