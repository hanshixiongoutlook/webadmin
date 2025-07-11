package com.hans.aggreation.webadmin.web.config.handler;

import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse> handleGeneralException(Exception ex) {
        log.error("", ex);
        return ResponseEntity.ok().body(RestResponse.error(ex.getMessage()));
    }

}
