package com.github.code.zxs.resource.model.dto;

import com.github.code.zxs.core.model.dto.BaseDTO;
import com.github.code.zxs.core.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
public class CollectDTO extends BaseDTO {
    private final ResourceDTO resourceDTO;
    private Set<Long> addFavIds;
    private Set<Long> delFavIds;

    public CollectDTO(Long createBy, ResourceDTO resourceDTO, Set<Long> addFavIds, Set<Long> delFavIds) {
        this(createBy, null, null, resourceDTO, addFavIds, delFavIds);
    }

    public CollectDTO(Long createBy, Date createTime, Date updateTime, ResourceDTO resourceDTO, Set<Long> addFavIds, Set<Long> delFavIds) {
        super(createBy, createTime, updateTime);
        Objects.requireNonNull(resourceDTO, "资源dto不能为空");
        Objects.requireNonNull(createBy, "收藏的用户不能为空");
        Date date = new Date();
        if (this.getCreateTime() == null)
            this.setCreateTime(date);
        if (this.getUpdateTime() == null)
            this.setUpdateTime(date);
        this.resourceDTO = resourceDTO;
        this.addFavIds = CollectionUtils.emptyIfNull(addFavIds);
        this.delFavIds = CollectionUtils.emptyIfNull(delFavIds);
    }
}
