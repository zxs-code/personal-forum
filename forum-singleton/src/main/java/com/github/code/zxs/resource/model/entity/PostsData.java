package com.github.code.zxs.resource.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.model.entity.Identity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
@TableName("tb_posts_data")
public class PostsData implements Identity, Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 浏览量
     */
    private Long views;
    /**
     * 浏览人数
     */
    private Long visitor;
    /**
     * 点赞数
     */
    private Long likes;
    /**
     * 点踩数
     */
    private Long dislikes;
    /**
     * 评论数
     */
    private Long comment;
    /**
     * 收藏数
     */
    private Long stars;
    /**
     * 得分
     */
    private Double score;
}
