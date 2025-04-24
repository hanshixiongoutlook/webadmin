package com.hans.aggreation.webadmin.web.config;

import com.hans.aggreation.webadmin.web.interceptor.LanguageInterceptor;
import com.hans.aggreation.webadmin.web.interceptor.TimeZoneInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 添加自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多语言设置拦截器，也可以使用Filter
        registry.addInterceptor(new LanguageInterceptor());
        // 时区设置拦截器，也可以使用Filter
        registry.addInterceptor(new TimeZoneInterceptor());
    }

    /**
     * 配置Rest返回值结构
     * 根据MIME自动识别xml或json
     * json需要此依赖
     * <dependency>
     * <groupId>com.fasterxml.jackson.core</groupId>
     * <artifactId>jackson-databind</artifactId>
     * </dependency>
     * xml需要次依赖
     * <dependency>
     * <groupId>com.fasterxml.jackson.dataformat</groupId>
     * <artifactId>jackson-dataformat-xml</artifactId>
     * </dependency>
     *
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON).favorPathExtension(true).mediaType("json", MediaType.APPLICATION_JSON).mediaType("xml", MediaType.APPLICATION_XML);
    }



}
