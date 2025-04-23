package com.hans.aggreation.webadmin.web.controller.multilang;

import com.hans.aggreation.webadmin.common.utils.localization.Localization;
import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequestMapping("/greeting")
@RestController("GreetingController")
public class GreetingController {
    @GetMapping(value = "/hello")
    public RestResponse<String> hello() {
        RestResponse<String> success = RestResponse.success(Localization.getLocalizedString("greeting.hello", List.of("12345")));
        return success;
    }
}
