package com.github.code.zxs.message.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.message.model.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 系统通知收件箱
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_notice_inbox")
public class NoticeInbox {
    private Long id;
    /**
     * 通知id
     */
    private Long noticeId;
    /**
     * 接收人id
     */
    private Long receiver;
    /**
     * 接收时间
     */
    private Date receiveTime;
    /**
     * 消息状态
     */
    private MessageStatus status;
}
