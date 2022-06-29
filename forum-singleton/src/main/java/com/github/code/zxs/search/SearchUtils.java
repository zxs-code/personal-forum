package com.github.code.zxs.search;

import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.search.model.bean.Searchable;
import com.github.code.zxs.search.model.document.PostsDocument;
import com.github.code.zxs.search.model.document.UserDocument;

import java.util.HashMap;
import java.util.Map;

public class SearchUtils {
    private static final Map<ResourceTypeEnum, Class<? extends Searchable>> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put(ResourceTypeEnum.POSTS, PostsDocument.class);
        TYPE_MAP.put(ResourceTypeEnum.USER, UserDocument.class);
    }

    public static Class<? extends Searchable> getDocumentClass(ResourceTypeEnum type) {
        return TYPE_MAP.get(type);
    }
}
