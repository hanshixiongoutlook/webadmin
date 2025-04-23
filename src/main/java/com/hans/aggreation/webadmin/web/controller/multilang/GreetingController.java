package com.hans.aggreation.webadmin.web.controller.multilang;

import com.hans.aggreation.webadmin.common.utils.localization.Localization;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequestMapping("/greeting")
@RestController("GreetingController")
public class GreetingController {
    @GetMapping("/hello")
    public String hello() {
        return Localization.getLocalizedString("greeting.hello", List.of("12345"));
    }
}
