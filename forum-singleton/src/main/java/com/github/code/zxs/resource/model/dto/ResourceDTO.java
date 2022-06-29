package com.github.code.zxs.resource.model.dto;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class ResourceDTO {
    private Long id;
    private ResourceTypeEnum type;

    public ResourceDTO(Long id, ResourceTypeEnum type) {
        Objects.requireNonNull(id, "资源id不能为空");
        Objects.requireNonNull(type, "资源类型不能为空");
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        Objects.requireNonNull(id);
        return id;
    }

    public ResourceTypeEnum getType() {
        Objects.requireNonNull(type);
        return type;
    }

    @Override
    public String toString() {
        return type + ":" + id;
    }
}
