package com.github.code.zxs.core.service.impl;




















@Service
@Slf4j
public class GroupServiceImpl extends ServiceImpl<GroupMapper, BaseGroup> implements GroupService {
    private final GroupMapper groupMapper;

    @Autowired
    public GroupServiceImpl(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    /**
     * 构造树形结构
     *
     * @param groups
     * @return
     */
    private List<TreeGroupDTO> getTreeGroup(List<BaseGroup> groups) {
        List<TreeGroupDTO> result = groups.stream().map(BaseGroup::toTreeGroupDTO).collect(Collectors.toList());
        Map<Integer, List<TreeGroupDTO>> map = result.stream().collect(Collectors.groupingBy(TreeGroupDTO::getParentId));
        result.forEach(item -> item.setChildren(map.get(item.getId()).stream().sorted().collect(Collectors.toList())));
        List<TreeGroupDTO> root = result.stream().filter(elem -> elem.getParentId() == 0).collect(Collectors.toList());
        log.info("获取树形分组结构:{}", root);
        return root;
    }

    public List<TreeGroupDTO> getAll(ResourceType type) {
        QueryWrapper<BaseGroup> wrapper = new QueryWrapper<>();
        wrapper.setEntity(BaseGroup.builder().type(type).build());
        List<BaseGroup> baseGroups = groupMapper.selectList(wrapper);
        log.info("获取链式分组结构:{}", baseGroups);
        return getTreeGroup(baseGroups);
    }

    public List<TreeGroupDTO> getChild(ResourceType type, Integer parentId) {
        QueryWrapper<BaseGroup> wrapper = new QueryWrapper<>();
        wrapper.setEntity(BaseGroup.builder().type(type).parentId(parentId).build());
        List<BaseGroup> baseGroups = groupMapper.selectList(wrapper);
        return baseGroups.stream().map(BaseGroup::toTreeGroupDTO).collect(Collectors.toList());
    }

    @Transactional
    public void insertBefore(ResourceType type, Integer beforeId, Integer curId, Integer parentId) {
        try {
            QueryWrapper<BaseGroup> wrapper = new QueryWrapper<>();
            wrapper.setEntity(BaseGroup.builder().type(type).id(curId).build());
            BaseGroup cur = groupMapper.selectOne(wrapper);

            if (beforeId == null) {
                //插入分组末尾
                cur.setParentId(parentId);
                groupMapper.updateMaxOrders(cur);
            } else {
                wrapper.setEntity(BaseGroup.builder().type(type).id(beforeId).build());
                BaseGroup before = groupMapper.selectOne(wrapper);
                if (!cur.getParentId().equals(before.getParentId())) {
                    //不同分组改变次序
                    groupMapper.AddOrdersAfter(before.getOrders(), -1);
                } else {
                    if (before.getOrders() < cur.getOrders())
                        //相同分组改变次序,且cur在before之后
                        groupMapper.AddOrdersBetween(before.getOrders(), cur.getOrders() - 1, 1);
                    else
                        //相同分组改变次序,且cur在before之前
                        groupMapper.AddOrdersBetween(cur.getOrders() + 1, before.getOrders(), -1);
                }
                cur.setParentId(parentId);
                cur.setOrders(before.getOrders());
                groupMapper.updateById(cur);
            }
        } catch (Exception e) {
            log.error("改变分组次序异常", e);
            throw new BaseException(ResponseStatus.FAIL);
        }
    }
}
