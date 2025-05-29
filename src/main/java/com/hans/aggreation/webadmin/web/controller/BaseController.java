package com.hans.aggreation.webadmin.web.controller;

import com.hans.aggreation.webadmin.common.consts.CommonConsts;
import com.hans.aggreation.webadmin.common.utils.DateFormatUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 对于直接从参数获取Date的场景，如，function(@RequestParam Date time)
 * 如需国际化则需要继承此Controller即可自动完成时区转换
 */
public class BaseController {
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String jsonDateFormatter;
    @Value("${spring.jackson.time-zone:GMT}")
    private String defaultTimeZone;
    @Value("${spring.jackson.i18n-enable:false}")
    private boolean i18nEable;
    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        // 从请求参数或 Header 中获取时区
        String timezone = request.getHeader(CommonConsts.HEADER_X_TIMEZONE);
        if (timezone == null || timezone.isEmpty()) {
            timezone = defaultTimeZone; // 默认值
        }
        TimeZone timeZone = TimeZone.getTimeZone(timezone);
        // 注册自定义的日期编辑器
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(jsonDateFormatter);
                    if (i18nEable) {
                        dateFormat.setTimeZone(timeZone);
                    }
                    Date parse = dateFormat.parse(text);
                    setValue(parse); // 将字符串转换为 Date
                } catch (ParseException e) {
                    throw new RuntimeException("无法解析的日期格式，"+text);
                }
            }

            @Override
            public String getAsText() {
                SimpleDateFormat sysFormatter = DateFormatUtils.getSysFormatter();
                return sysFormatter.format(sysFormatter);
            }
        });
    }
}
