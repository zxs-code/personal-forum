package com.github.code.zxs.core.config;

import com.github.code.zxs.auth.model.enums.UserStateEnum;
import com.github.code.zxs.core.model.enums.BaseEnum;
import com.github.code.zxs.core.util.CollectionUtils;
import com.github.code.zxs.resource.model.enums.GenderEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

@Configuration
public class ElasticsearchConfig {
    @Bean
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(CollectionUtils.asList(
                BaseEnum.EnumToIntegerConverter.INSTANCE,
                UserStateEnum.IntegerToEnumConverter.INSTANCE,
                GenderEnum.IntegerToEnumConverter.INSTANCE
        ));
    }
}
