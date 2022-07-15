package com.github.code.zxs.core.support.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.code.zxs.core.util.SpringContextUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class CustomJsonDeserializer extends JsonDeserializer<Object> {


    public CustomJsonDeserializer() {
        super(SpringContextUtils.getBean(ObjectMapper.class));
        this.typeMapper.addTrustedPackages("*");
    }

//    private static ObjectMapper customizedObjectMapper() {
//        ObjectMapper mapper = JacksonUtils.enhancedObjectMapper();
//        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        return mapper;
//    }
}