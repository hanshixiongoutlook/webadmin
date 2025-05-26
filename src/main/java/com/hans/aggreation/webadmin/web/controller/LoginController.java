package com.hans.aggreation.webadmin.web.controller;

import com.hans.aggreation.webadmin.core.model.UserModel;
import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import com.hans.aggreation.webadmin.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    @GetMapping("/getUserInfo")
    public RestResponse<UserModel> getUserInfo(@RequestParam String username) {
        UserModel user = userService.getUser(username);
        return RestResponse.success(user);
    }
}
