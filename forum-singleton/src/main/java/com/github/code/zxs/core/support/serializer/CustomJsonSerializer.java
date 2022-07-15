package com.github.code.zxs.core.support.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.code.zxs.core.util.SpringContextUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomJsonSerializer extends JsonSerializer<Object> {


    public CustomJsonSerializer() {
        super(SpringContextUtils.getBean(ObjectMapper.class));
    }
//
//    private static ObjectMapper customizedObjectMapper() {
//        ObjectMapper mapper = JacksonUtils.enhancedObjectMapper();
//        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        return mapper;
//    }

}