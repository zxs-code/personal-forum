package com.github.code.zxs.message.model.bo;

import com.github.code.zxs.resource.model.bo.AuthorBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionBO {
    private Long id;
    private AuthorBO talker;
    private PrivateLetterBO lastMessage;
    private Long unreadCount;
}
