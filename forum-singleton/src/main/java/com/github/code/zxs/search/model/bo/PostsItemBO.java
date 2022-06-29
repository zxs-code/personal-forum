package com.github.code.zxs.search.model.bo;

import com.github.code.zxs.resource.model.bo.AuthorBO;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsItemBO {
    private Long id;
    private String title;
    private String summary;
    private Boolean top;
    private Boolean good;
    private Boolean locked;
    private Boolean hide;
    private Long views;
    private Long likes;
    private Long dislikes;
    private Long comment;
    private AuthorBO author;
    private String previewImg;
    private String srcImg;
    private Date createTime;
    private Date updateTime;
}
