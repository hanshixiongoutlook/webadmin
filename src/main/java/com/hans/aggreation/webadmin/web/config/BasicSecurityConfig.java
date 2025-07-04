package com.hans.aggreation.webadmin.web.config;

import com.hans.aggreation.webadmin.core.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Slf4j
@ConditionalOnExpression("${spring.security.enabled:false} and '${spring.security.type}'.equals('basic')")
@EnableWebSecurity(debug = false)
@Configuration
public class BasicSecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.err.println("========= Enable BasicSecurityConfig.");
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/css/**")
                        .permitAll().anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser") // 对应登录表单的 action，用于登录信息校验
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                ).authenticationProvider(authenticationProvider())
                // 开启Basic认证，支持通过在Header中添加 Authorization方式调用接口
                // Authorization Basic Base64Encode(username:password)
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()); // 禁用 CSRF 保护，前端需要post时，通常禁用
        return http.build();
    }

    /**
     * 配置自定义的登录接口
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * 密码加密方式
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
