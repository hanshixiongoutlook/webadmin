package com.hans.aggreation.webadmin.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateFormatUtils {
    public static SimpleDateFormat getSysFormatter() {
        Environment env = SpringContextUtils.getBean(Environment.class);
        String timezone = env.getProperty("spring.jackson.time-zone");
        String formatter = env.getProperty("spring.jackson.date-format");
        if (StringUtils.isBlank(formatter)) {
            formatter = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);
        if (StringUtils.isNotBlank(timezone)) {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        return simpleDateFormat;
    }

}
