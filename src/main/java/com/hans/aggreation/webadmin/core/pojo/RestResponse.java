package com.hans.aggreation.webadmin.core.pojo;

import java.io.Serializable;


public class RestResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String STATUS_SUCCESS = "S";
    public static final String STATUS_ERROR = "E";

    private String status;

    private String message;

    private T data;

    public RestResponse() {
    }

    public static <T> RestResponse<T> success() {
        RestResponse<T> restResponse = new RestResponse<>();
        return restResponse.status(STATUS_SUCCESS);
    }
    public static <T> RestResponse<T> success(T data) {
        RestResponse<T> restResponse = new RestResponse<>();
        return restResponse.status(STATUS_SUCCESS).data(data);
    }
    public static <T> RestResponse<T> success(String message, T data) {
        RestResponse<T> restResponse = new RestResponse<>();
        return restResponse.status(STATUS_SUCCESS).message(message).data(data);
    }
    public static <T> RestResponse<T> error() {
        RestResponse<T> restResponse = new RestResponse<>();
        return restResponse.status(STATUS_ERROR);
    }
    public static <T> RestResponse<T> error(String message) {
        RestResponse<T> restResponse = new RestResponse<>();
        return restResponse.status(STATUS_ERROR).message(message);
    }
    public static <T> RestResponse<T> error(String message, T data) {
        RestResponse<T> restResponse = new RestResponse<>();
        return restResponse.status(STATUS_ERROR).message(message).data(data);
    }

    public RestResponse<T> status(String status) {
        this.status = status;
        return this;
    }
    public RestResponse<T> data(T data) {
        this.data = data;
        return this;
    }
    public RestResponse<T> message(String message) {
        this.message = message;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
