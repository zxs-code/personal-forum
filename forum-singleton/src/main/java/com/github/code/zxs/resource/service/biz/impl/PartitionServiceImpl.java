package com.github.code.zxs.resource.service.biz.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.resource.mapper.PartitionMapper;
import com.github.code.zxs.resource.model.entity.Partition;
import com.github.code.zxs.resource.service.biz.base.PartitionService;

import org.springframework.stereotype.Service;

@Service
public class PartitionServiceImpl extends ServiceImpl<PartitionMapper,Partition> implements PartitionService {
}
