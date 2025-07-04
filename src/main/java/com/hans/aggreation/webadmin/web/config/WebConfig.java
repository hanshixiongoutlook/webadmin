package com.hans.aggreation.webadmin.web.config;

import com.hans.aggreation.webadmin.web.config.interceptor.LanguageInterceptor;
import com.hans.aggreation.webadmin.web.config.interceptor.TimeZoneInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

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
        registry.addInterceptor(new LanguageInterceptor(localeResolver()));
        // 时区设置拦截器，也可以使用Filter
        registry.addInterceptor(new TimeZoneInterceptor());
    }

    /**
     * 配置时区解析器
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver slr = new AcceptHeaderLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
    /**
     * 配置Rest返回值结构
     * 根据MIME自动识别xml或json
     * Http Header
     *      Accept -> application/xml 返回值为xml格式
     *      Accept -> application/json 返回值为json格式
     *      Accept -> * 返回值为json格式(defaultContentType)
     *
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
        configurer
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .defaultContentType(MediaType.APPLICATION_JSON);
    }



}
