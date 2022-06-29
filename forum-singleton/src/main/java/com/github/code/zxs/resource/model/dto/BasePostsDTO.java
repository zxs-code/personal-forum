package com.github.code.zxs.resource.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePostsDTO {
    @NotNull(message = "标题不能为空")
    @Length(min = 2, max = 64,message = "标题字数需要在2-64之间")
    private String title;
    @NotNull(message = "内容不能为空")
    @Length(max = 20000,message = "正文内容需要在20000字以内")
    private String content;
    @NotNull(message = "至少需要一个标签")
    @Size(min = 1,max = 10,message = "标签数量为1-10")
    private List<String> tags;
}
