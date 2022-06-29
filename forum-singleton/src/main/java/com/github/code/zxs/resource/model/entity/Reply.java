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
@AllArgsConstructor
@NoArgsConstructor
@With
@FieldNameConstants
@TableName("tb_reply")
public class Reply extends BaseLogicEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private ResourceTypeEnum resourceType;
    private Long resourceId;
    /**
     * 回复的评论id
     */
    private Long commentId;
    /**
     * 楼层
     */
    private Long seq;
    /**
     * 对话id,一段对话中第一个回复id
     */
    private Long dialogId;
    /**
     * 回复的对象的id
     */
    private Long toUserId;
    /**
     * 回复内容
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
}
