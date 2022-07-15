package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.resource.model.dto.ResourceDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class UserViewDTO {
    private Long userId;
    private Date timestamp;
    private ResourceDTO resourceDTO;

    public UserViewDTO(Long userId, Date timestamp, ResourceDTO resourceDTO) {
        Objects.requireNonNull(userId, "用户id不能为空");
        Objects.requireNonNull(timestamp, "时间戳不能为空");
        Objects.requireNonNull(resourceDTO, "资源不能为空");
        Objects.requireNonNull(resourceDTO.getId(), "资源id不能为空");
        Objects.requireNonNull(resourceDTO.getType(), "资源类型不能为空");
        this.userId = userId;
        this.timestamp = timestamp;
        this.resourceDTO = resourceDTO;
    }
}
