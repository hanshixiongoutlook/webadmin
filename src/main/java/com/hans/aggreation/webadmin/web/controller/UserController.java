package com.hans.aggreation.webadmin.web.controller;

import com.hans.aggreation.webadmin.core.pojo.CustomUserDetails;
import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/current")
    public RestResponse<CustomUserDetails> getCurrentUser() {
        CustomUserDetails customUserDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return RestResponse.success(customUserDetails);
    }
}
