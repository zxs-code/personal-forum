package com.github.code.zxs.message.model.bo;

import com.github.code.zxs.message.model.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateLetterBO {
    private Long id;
    private String content;
    private Long sender;
    private Long receiver;
    private Date sendTime;
    private MessageStatus messageStatus;
}
