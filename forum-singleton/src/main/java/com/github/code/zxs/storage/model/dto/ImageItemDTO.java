package com.github.code.zxs.storage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageItemDTO extends FileItemDTO {
    private Integer width;
    private Integer height;

    public ImageItemDTO(FileItemDTO fileItemDTO) {
        BeanUtils.copyProperties(fileItemDTO, this);
    }

    public ImageItemDTO(FileItemDTO fileItemDTO, Integer width, Integer height) {
        this(fileItemDTO);
        if (width == null || width <= 0 || height == null || height <= 0)
            throw new IllegalArgumentException("图片的宽和高需要大于0");
        this.width = width;
        this.height = height;
    }
}
