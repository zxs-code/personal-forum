package com.github.code.zxs.search.strategy.wrap;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.converter.UserConverter;
import com.github.code.zxs.search.model.bean.Searchable;
import com.github.code.zxs.search.model.document.UserDocument;
import org.springframework.stereotype.Component;

@Component
public class UserItemWrapper implements ItemWrapper {
    private final UserConverter userConverter = UserConverter.defaultConverter();

    @Override
    public ResourceTypeEnum getResourceType() {
        return ResourceTypeEnum.USER;
    }

    @Override
    public Object wrap(Searchable document) {
        UserDocument user = (UserDocument) document;
        return userConverter.toItemBO(user);
    }
}
