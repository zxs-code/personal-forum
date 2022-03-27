package com.github.code.zxs.resource.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.entity.BaseLogicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_posts")
public class Posts extends BaseLogicEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 主题id
     */
    private Integer topicId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
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
    private Boolean lock;
    /**
     * 是否隐藏，隐藏后需要相应的权限才能阅览
     */
    private Boolean hide;
    /**
     * 是否匿名
     */
    private Boolean anonymous;
    /**
     * 浏览量
     */
    private Integer view;
    /**
     * 浏览人数
     */
    private Integer visitor;
    /**
     * 点赞数
     */
    private Integer like;
    /**
     * 点踩数
     */
    private Integer dislike;
    /**
     * 评论数
     */
    private Integer replyCount;
    /**
     * 收藏数
     */
    private Integer stars;
}
