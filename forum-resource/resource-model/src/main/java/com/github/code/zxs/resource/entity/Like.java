package com.github.code.zxs.resource.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.entity.BaseUserEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_like")
public class Like extends BaseUserEvent {

}
