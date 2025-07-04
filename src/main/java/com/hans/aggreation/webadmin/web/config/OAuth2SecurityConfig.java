package com.hans.aggreation.webadmin.web.config;

import com.hans.aggreation.webadmin.common.utils.JacksonUtil;
import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import com.hans.aggreation.webadmin.core.service.CustomUserDetailsService;
import com.hans.aggreation.webadmin.web.config.filter.JwtAuthenticationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import java.io.IOException;

/**
 * OAuth2 认证
 */
@Slf4j
@ConditionalOnExpression("${spring.security.enabled:false} and '${spring.security.type}'.equals('OAuth2')")
@EnableWebSecurity(debug = false)
@Configuration
public class OAuth2SecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 认证管理器，登录时使用
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.err.println("========= Enable OAuth2SecurityConfig.");
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/oauth2/login", "/css/**")
                        .permitAll().anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class)
                .sessionManagement(SessionManagementConfigurer::disable) // 采用Token方式认证，因此可以关闭session管理
                .formLogin(AbstractHttpConfigurer::disable) // 禁用原来的登录页面
                .logout(LogoutConfigurer::disable) // 禁用原来的登出
                .exceptionHandling(ex->ex.authenticationEntryPoint((request, response, authException) -> {
                    log.debug("认证失败...");
                    RestResponse<String> result = RestResponse.error(authException.getMessage());
                    response.setContentType("application/json");
                    response.getWriter().println(JacksonUtil.toJson(result));
                }));
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
