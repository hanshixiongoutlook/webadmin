package com.hans.aggreation.webadmin.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.TimeZone;

/**
 */
public class TimeZoneInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String timeZoneStr = request.getHeader("timezone");
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        if (StringUtils.isNotBlank(timeZoneStr)) {
            timeZone = TimeZone.getTimeZone(timeZoneStr);
        }
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        requestAttributes.setAttribute("timezone", timeZone, RequestAttributes.SCOPE_REQUEST);
        return true;
    }
}
