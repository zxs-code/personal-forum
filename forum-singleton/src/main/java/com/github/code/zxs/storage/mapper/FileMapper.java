package com.github.code.zxs.storage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.code.zxs.storage.model.entity.FileItem;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMapper extends BaseMapper<FileItem> {

    /**
     * 根据hash值查询文件路径
     *
     * @param hash
     * @return
     */
    @Select("SELECT path FROM tb_file_item WHERE hash=#{hash} ORDER BY timestamp DESC LIMIT 1")
    public String findPathByHash(String hash);
}
