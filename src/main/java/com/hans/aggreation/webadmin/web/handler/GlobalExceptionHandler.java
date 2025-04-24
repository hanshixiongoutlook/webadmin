package com.hans.aggreation.webadmin.web.handler;

import com.hans.aggreation.webadmin.core.pojo.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.ok().body(RestResponse.error(ex.getMessage()));
    }

}
