package com.github.code.zxs.resource.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.model.entity.BaseLogicEntity;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldNameConstants;

@Data
@With
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_comment")
public class Comment extends BaseLogicEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 评论的楼层
     */
    private Long seq;
    /**
     * 资源id
     */
    private Long resourceId;
    /**
     * 评论的资源类型
     */
    private ResourceTypeEnum resourceType;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 点赞数
     */
    private Long likes;
    /**
     * 点踩数
     */
    private Long dislikes;
    /**
     * 回复数
     */
    private Long reply;
}
