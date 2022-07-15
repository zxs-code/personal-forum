package com.github.code.zxs.message.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.config.MybatisPlusConfig;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.message.model.enums.MessageStatus;
import com.github.code.zxs.resource.model.enums.ActionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * 使用动态表名 {@link MybatisPlusConfig#mybatisPlusInterceptor()}
 */
@TableName("tb_remind")
public class Remind {
    private Long id;
    /**
     * 事件源类型，如帖子
     */
    private ResourceTypeEnum resourceType;
    /**
     * 事件源id，如帖子id
     */
    private Long resourceId;
    /**
     * 标题
     */
    private String title;
    /**
     * 用户行为，如点赞等
     */
    private ActionEnum action;
    /**
     * 消息状态
     */
    private MessageStatus status;
    /**
     * 最后一个操作者id
     */
    private Long sender;
    /**
     * 操作者的人数
     */
    private Long counts;
    /**
     * 接收者id
     */
    private Long receiver;
    /**
     * 提醒时间
     */
    private Date remindTime;
}
