package com.github.code.zxs.core.model.enums;

import org.springframework.core.convert.converter.Converter;

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


    enum EnumToIntegerConverter implements Converter<BaseEnum, Integer> {

        INSTANCE;

        @Override
        public Integer convert(BaseEnum baseEnum) {
            return baseEnum.getCode();
        }
    }
}
