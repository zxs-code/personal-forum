package com.github.code.zxs.resource.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsSummaryBO {
    private Long id;
    private String title;
    private String summary;
    private Boolean top;
    private Boolean good;
    private Boolean locked;
    private Boolean hide;
    private Long views;
    private Long reply;
    private Long comment;
    private AuthorBO author;
    private String previewImg;
    private String srcImg;
    private Date createTime;
}
