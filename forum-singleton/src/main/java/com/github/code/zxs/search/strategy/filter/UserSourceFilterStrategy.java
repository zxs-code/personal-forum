package com.github.code.zxs.search.strategy.filter;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class UserSourceFilterStrategy implements SourceFilterStrategy {

    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.USER;
    }

}
