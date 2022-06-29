package com.github.code.zxs.core.model.enums;

public interface BaseEnum {
    int getCode();

    static <T extends BaseEnum> T valueOf(Class<T> enumType, int code) {
        if (enumType == null) {
            return null;
        }
        T[] enumConstants = enumType.getEnumConstants();
        if (enumConstants == null) {
            return null;
        }
        for (T enumConstant : enumConstants) {
            if (code == enumConstant.getCode()) {
                return enumConstant;
            }
        }
        return null;
    }

}
