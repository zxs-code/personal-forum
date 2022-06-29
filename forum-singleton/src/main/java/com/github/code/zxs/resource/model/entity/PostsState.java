package com.github.code.zxs.resource.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.model.entity.Identity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tb_posts_state")
public class PostsState implements Identity {
    private Long id;
    /**
     * 是否置顶
     */
    private Boolean top;
    /**
     * 是否精华
     */
    private Boolean good;
    /**
     * 是否锁定，锁定后帖子为只读，不能进行更新或删除操作，也不能回复。
     */
    private Boolean locked;
    /**
     * 是否隐藏，隐藏后需要相应的权限才能阅览
     */
    private Boolean hide;
    /**
     * 是否匿名
     */
    private Boolean anonymous;
}
