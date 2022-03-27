package com.github.code.zxs.resource.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.entity.BaseLogicEntity;
import com.github.code.zxs.user.Device;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_reply")
public class Reply extends BaseLogicEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 主题id
     */
    private Integer topicId;
    /**
     * 帖子id
     */
    private Integer postsId;
    /**
     * 回复给哪条评论的id，为0则是对帖子的回复
     */
    private Integer replyTo = 0;
    /**
     * 回复给哪个用户的id，为0则是对回复的回复
     */
    private Integer replyToUserId = 0;
    /**
     * 回复给哪个用户的昵称
     */
    private String replyToNickname;
    /**
     * 内容
     */
    private String content;
    /**
     * 点赞数
     */
    private Integer like;
    /**
     * 点踩数
     */
    private Integer dislike;
    /**
     * 回复数
     */
    private Integer replyCount;
    /**
     * 是否匿名
     */
    private Boolean anonymous;
    /**
     * 设备类型
     */
    private Device device;
}
