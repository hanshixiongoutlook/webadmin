package com.hans.aggreation.webadmin.web.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 将Response日期统一转换为系统指定时区的时间
 */
@Component
public class DefaultTimeZoneDateSerializer extends JsonSerializer<Date> {
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String jsonDateFormatter;
    @Value("${spring.jackson.time-zone:GMT}")
    private String defaultTimeZone;
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(jsonDateFormatter);
        dateFormat.setTimeZone(TimeZone.getTimeZone(defaultTimeZone));
        gen.writeString(dateFormat.format(date));
    }
}
