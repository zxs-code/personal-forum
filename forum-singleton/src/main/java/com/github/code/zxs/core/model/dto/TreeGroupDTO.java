package com.github.code.zxs.core.model.dto;


import com.github.code.zxs.core.model.entity.BaseGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeGroupDTO extends BaseGroup {
    private List<TreeGroupDTO> children;
}
