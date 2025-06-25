package com.hans.aggreation.webadmin.web.controller;

import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import com.hans.aggreation.webadmin.core.pojo.dto.LoginDTO;
import com.hans.aggreation.webadmin.core.service.OAuth2LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="OAuth2LoginController", description = "OAuth2认证")
@RestController
@RequestMapping("/oauth2")
public class OAuth2LoginController {

    @Autowired
    private OAuth2LoginService oAuth2LoginService;

    @Operation(description = "用户登录")
    @PostMapping("/login")
    public RestResponse<String> getCurrentUser(@RequestBody LoginDTO loginDTO) {
        return oAuth2LoginService.login(loginDTO);
    }
}
