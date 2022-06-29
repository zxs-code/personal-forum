package com.github.code.zxs.search.strategy.wrap;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.model.bean.Searchable;

public interface ItemWrapper {
    ResourceTypeEnum getResourceType();

    Object wrap(Searchable document);
}
