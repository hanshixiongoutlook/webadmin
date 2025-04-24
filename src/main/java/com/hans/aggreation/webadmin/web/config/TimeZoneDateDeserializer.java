package com.hans.aggreation.webadmin.web.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 将前端传入的日期转换成指定时区的时间
 */
@Component
public class TimeZoneDateDeserializer extends JsonDeserializer<Date> {
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String jsonDateFormatter;
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        TimeZone timezone = (TimeZone) RequestContextHolder.currentRequestAttributes()
                .getAttribute("timezone", RequestAttributes.SCOPE_REQUEST);
        SimpleDateFormat dateFormat = new SimpleDateFormat(jsonDateFormatter);
        dateFormat.setTimeZone(timezone);
        try {
            return dateFormat.parse(jsonParser.getText());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
