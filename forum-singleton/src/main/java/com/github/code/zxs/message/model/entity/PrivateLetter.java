package com.github.code.zxs.message.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.message.model.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 私信
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_private_letter")
public class PrivateLetter {
    private Long id;
    /**
     * 会话id
     */
    private Long sessionId;
    /**
     * 内容，最大字数512
     */
    private String content;
    /**
     * 发送人id
     */
    private Long sender;
    /**
     * 接收人id
     */
    private Long receiver;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 对于发送者的消息状态
     */
    private MessageStatus senderStatus;
    /**
     * 对于接收者的消息状态
     */
    private MessageStatus receiverStatus;
}
