package com.github.code.zxs.core.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.core.dto.TreeGroupDTO;
import com.github.code.zxs.core.enums.ResourceType;
import lombok.*;
import org.springframework.beans.BeanUtils;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_group")
public class BaseGroup extends BaseLogicEntity implements Comparable<BaseGroup> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId = 0;
    private String name;
    private ResourceType type;
    private Integer orders = 0;

    public TreeGroupDTO toTreeGroupDTO() {
        TreeGroupDTO treeGroupDTO = new TreeGroupDTO();
        BeanUtils.copyProperties(treeGroupDTO, this);
        return treeGroupDTO;
    }

    @Override
    public int compareTo(BaseGroup o) {
        return this.orders - o.orders;
    }
}
