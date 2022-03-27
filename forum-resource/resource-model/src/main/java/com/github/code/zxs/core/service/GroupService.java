package com.github.code.zxs.core.service;









/**
 * 分组服务
 */
public interface GroupService extends IService<BaseGroup> {
    /**
     * 获取所有分组，并以树形结构返回
     * @param type 资源类型
     * @return
     */
    List<TreeGroupDTO> getAll(ResourceType type);

    /**
     * 获取某个分组的子分组
     * @param type 资源类型
     * @param parentId 父分组id
     * @return
     */
    List<TreeGroupDTO> getChild(ResourceType type, Integer parentId);

    /**
     * 改变分组次序
     * @param type 资源类型
     * @param beforeId 插入某个节点之前
     * @param curId   当前节点id
     * @param parentId 父分组id
     */
    void insertBefore(ResourceType type, Integer beforeId, Integer curId, Integer parentId);
}
