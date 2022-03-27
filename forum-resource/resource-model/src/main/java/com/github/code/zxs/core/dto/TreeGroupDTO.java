package com.github.code.zxs.core.dto;









@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeGroupDTO extends BaseGroup {
    private List<TreeGroupDTO> children;
}
