package com.github.code.zxs.storage.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.code.zxs.storage.model.dto.FileItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_file_item")
public class FileItem {
    @TableId("`key`")
    private String key;
    private String name;
    private Long size;
    private String url;
    private String path;
    private Date timestamp;
    private Long uploaderId;
    private String hash;
    @TableLogic(value = "NULL", delval = "NOW()")
    private Date deleteTime;

    public FileItemDTO toFileItemDTO() {
        FileItemDTO fileItemDTO = new FileItemDTO();
        BeanUtils.copyProperties(this, fileItemDTO);
        return fileItemDTO;
    }
}
