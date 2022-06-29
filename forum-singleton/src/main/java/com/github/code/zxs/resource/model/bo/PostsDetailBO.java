package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsDetailBO {
    private Long id;
    private String title;
    private String content;
    private Long views;
    private Long likes;
    private Long dislikes;
    private Long stars;
    private Long comment;
    private Boolean top;
    private Boolean good;
    private Boolean locked;
    private Boolean hide;
    private Boolean isAuthor;
    private LikeStateEnum likeState;
    private Boolean isStars;
    private Date createTime;
    private Date updateTime;
    private AuthorBO author;
    private List<String> tags;
}
