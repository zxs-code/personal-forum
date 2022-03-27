package com.github.code.zxs.core.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.code.zxs.core.entity.BaseGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface GroupMapper extends BaseMapper<BaseGroup> {

    /**
     * 将分组顺序设为同级下的最大值
     *
     * @param group
     * @return
     */
    @Update({"UPDATE tb_group t1,",
            "(SELECT IFNULL(MAX(orders),1) max_order FROM tb_group WHERE type = #{type} AND parent_id = #{parent_id}) t2",
            "SET t1.orders = t2.max_order + 1",
            "t1.parent_id = #{parent_id}",
            "WHERE id = #{id};"})
    Integer updateMaxOrders(BaseGroup group);
//
//    /**
//     * 获取同级下的最大orders
//     * @param type
//     * @param parentId
//     * @return
//     */
//    @Select("SELECT IFNULL(MAX(orders),1) max_order FROM tb_group WHERE type = #{type} AND parent_id = #{parent_id}")
//    Integer getMaxOrders(@Param("type") ResourceType type,@Param("parent_id") Integer parentId);

    @Update({"UPDATE tb_group",
            "SET orders = orders + #{amount}",
            "WHERE orders BETWEEN #{start} AND #{end};"})
    Integer AddOrdersBetween(@Param("start") Integer startOrder, @Param("end") Integer endOrder, @Param("amount") Integer amount);


    @Update({"UPDATE tb_group",
            "SET orders = orders + #{amount}",
            "WHERE orders >= #{start};"})
    Integer AddOrdersAfter(@Param("start") Integer startOrder, @Param("amount") Integer amount);

}
