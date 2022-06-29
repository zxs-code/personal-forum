package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.core.model.dto.BaseDTO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventDTO extends BaseDTO {
    private String path;
    private String method;
    private String description;
    private ResourceDTO resourceDTO;

    public UserEventDTO(Long createBy, Date createTime, Date updateTime, String path, String method, String description, ResourceDTO resourceDTO) {
        super(createBy, createTime, updateTime);
        this.path = path;
        this.method = method;
        this.description = description;
        this.resourceDTO = resourceDTO;
    }

    public UserEventDTO(Long createBy, String path, String method, String description, ResourceDTO resourceDTO) {
        super(createBy);
        this.path = path;
        this.method = method;
        this.description = description;
        this.resourceDTO = resourceDTO;
    }
}
