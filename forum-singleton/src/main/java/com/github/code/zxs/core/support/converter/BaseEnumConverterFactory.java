package com.github.code.zxs.core.support.converter;

import com.github.code.zxs.core.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class BaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return source -> {
            int code = Integer.parseInt(source);
            return BaseEnum.valueOf(targetType, code);
        };
    }
}
