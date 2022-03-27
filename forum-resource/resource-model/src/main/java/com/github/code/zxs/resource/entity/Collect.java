package com.github.code.zxs.resource.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.entity.BaseUserEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收藏
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_collect")
public class Collect extends BaseUserEvent {
    //收藏夹id
    private Integer favoritesId;
}
