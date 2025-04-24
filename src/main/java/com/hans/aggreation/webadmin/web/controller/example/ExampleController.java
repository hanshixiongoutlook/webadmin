package com.hans.aggreation.webadmin.web.controller.example;

import com.hans.aggreation.webadmin.common.utils.JacksonUtil;
import com.hans.aggreation.webadmin.common.utils.localization.Localization;
import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import com.hans.aggreation.webadmin.core.pojo.example.TimeZoneExampleBean;
import com.hans.aggreation.webadmin.web.controller.BaseI18nController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


@RequestMapping("/example")
@RestController("ExampleController")
public class ExampleController extends BaseI18nController {
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
    @GetMapping(value = "/timeZone2")
    public Date timeZone2(@RequestBody TimeZoneExampleBean bean, @RequestParam("time") Date time) {
        log.info("bean={}, time={}", JacksonUtil.toJson(bean), time);
        return time;
    }
}
