package com.hans.aggreation.webadmin.web.controller;

import com.hans.aggreation.webadmin.core.pojo.CustomUserDetails;
import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="UserController", description = "用户信息")
@RestController
@RequestMapping("/user")
public class UserController {

    @Operation(description = "获取当前用户")
    @GetMapping("/current")
    public RestResponse<CustomUserDetails> getCurrentUser() {
        CustomUserDetails customUserDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return RestResponse.success(customUserDetails);
    }
}
