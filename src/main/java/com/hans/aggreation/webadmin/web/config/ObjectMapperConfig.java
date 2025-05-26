package com.hans.aggreation.webadmin.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hans.aggreation.webadmin.web.config.timezone.DefaultTimeZoneDateSerializer;
import com.hans.aggreation.webadmin.web.config.timezone.TimeZoneDateDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * 自定义Json解析器
 */
@ConditionalOnProperty(name="spring.jackson.i18n-enable", havingValue = "true")
@Configuration
public class ObjectMapperConfig {
    @Autowired
    private TimeZoneDateDeserializer timeZoneDateDeserializer;
    @Autowired
    private DefaultTimeZoneDateSerializer defaultTimeZoneDateSerializer;
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(getInternationalizationDateModule());
        return mapper;
    }

    /**
     * 国际化时区转换器
     * Deserializer：按照请求头中的timezone解析Date
     * Serializer：将Response中的Date同意转换成UTC时间
     * @return
     */
    private SimpleModule getInternationalizationDateModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, defaultTimeZoneDateSerializer);
        module.addDeserializer(Date.class, timeZoneDateDeserializer);
        return module;
    }
}
