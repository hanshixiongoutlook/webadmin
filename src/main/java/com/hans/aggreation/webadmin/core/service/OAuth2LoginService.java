package com.hans.aggreation.webadmin.core.service;

import com.hans.aggreation.webadmin.common.utils.JwtUtils;
import com.hans.aggreation.webadmin.core.pojo.CustomUserDetails;
import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import com.hans.aggreation.webadmin.core.pojo.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OAuth2LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    public RestResponse<String> login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            if (authenticate!=null && authenticate.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticate);
                CustomUserDetails user = (CustomUserDetails)authenticate.getPrincipal();
                String token = JwtUtils.generateToken(user.getUsername());
                return RestResponse.success(token);
            }
        } catch (AuthenticationException e) {
            log.debug("认证失败", e);
        }
        return RestResponse.error("认证失败");
    }

}
