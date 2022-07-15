package com.github.code.zxs.message.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.message.model.enums.MessageStatus;
import com.github.code.zxs.message.model.enums.ReceiveType;
import com.github.code.zxs.message.model.enums.SendType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 系统通知
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_notice")
public class Notice {
    private Long id;
    /**
     * 标题，最大字数为32
     */
    private String title;
    /**
     * 内容，最大字数为512
     */
    private String content;
    /**
     * 通知的发送方式
     */
    private SendType sendType;
    /**
     * 通知的接收方式
     */
    private ReceiveType receiveType;
    /**
     * 发送人id
     */
    private Long sender;
    /**
     * 接收人id，仅在 {@link #sendType} 为 {@link SendType#UNICAST} 时有效
     */
    private Long receiver;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 过期时间，{@link #receiveType} 为 {@link ReceiveType#PULL}时有效。
     * 当接收方式设置为拉取且已到达过期时间时，用户不会收到该通知。
     */
    private Date expireTime;
    /**
     * 消息状态
     */
    private MessageStatus status;
}
