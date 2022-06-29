package com.github.code.zxs.auth.model.dto;


import com.github.code.zxs.core.model.entity.BaseEntity;
import lombok.Data;

@Data
public class RoleDTO extends BaseEntity {
    private Long id;
    private String name;
    private String description;
}
