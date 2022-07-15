package com.github.code.zxs.message.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 会话，包含会话列表的两个用户
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_session")
public class Session {
    private Long id;
    private Long user1;
    private Long user2;
    private Date lastTime;
    @TableLogic(value = "NULL", delval = "NOW()")
    private Date deleteTime;
}
