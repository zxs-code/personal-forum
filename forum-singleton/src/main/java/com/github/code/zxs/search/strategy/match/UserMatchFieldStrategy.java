package com.github.code.zxs.search.strategy.match;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.model.document.UserDocument;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class UserMatchFieldStrategy implements MatchFieldStrategy {

    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.USER;
    }

    @Override
    public @NotNull String[] getMatchField() {
        return new String[]{
                UserDocument.Fields.nickname
        };
    }
}
