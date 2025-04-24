package com.hans.aggreation.webadmin.web.controller.example;

import com.hans.aggreation.webadmin.common.utils.JacksonUtil;
import com.hans.aggreation.webadmin.common.utils.localization.Localization;
import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import com.hans.aggreation.webadmin.core.pojo.example.TimeZoneExampleBean;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


@RequestMapping("/example")
@RestController("ExampleController")
public class ExampleController {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @GetMapping(value = "/langTest")
    public RestResponse<String> langTest(@RequestParam("msg")String msg) {
        RestResponse<String> success = RestResponse.success(Localization.getLocalizedString("greeting.hello", List.of(msg)));
        return success;
    }

    @GetMapping(value = "/timeZone")
    public RestResponse<TimeZoneExampleBean> timeZone(@RequestBody TimeZoneExampleBean bean, @RequestParam("time") Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        RestResponse<TimeZoneExampleBean> success = RestResponse.success(bean).message(simpleDateFormat.format(time));
        log.info("bean={}, time={}", JacksonUtil.toJson(bean), time);
        return success;
    }
    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        // 从请求参数或 Header 中获取时区
        String timezone = request.getHeader("timezone");
        if (timezone == null || timezone.isEmpty()) {
            timezone = "UTC"; // 默认值
        }
        TimeZone timeZone = TimeZone.getTimeZone(timezone);
        // 注册自定义的日期编辑器
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            {
                dateFormat.setTimeZone(timeZone);
            }

            @Override
            public void setAsText(String text) {
                try {
                    Date parse = dateFormat.parse(text);
                    setValue(parse); // 将字符串转换为 Date
                } catch (ParseException e) {
                    throw new RuntimeException("无法解析的日期格式，"+text);
                }
            }

            @Override
            public String getAsText() {
                TimeZone timeZone = TimeZone.getTimeZone("UTC");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(timeZone);
                Date time = (Date) getValue();
                return simpleDateFormat.format(time);
            }
        });
    }
}
