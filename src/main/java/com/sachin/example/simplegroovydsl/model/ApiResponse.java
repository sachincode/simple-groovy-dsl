package com.sachin.example.simplegroovydsl.model;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("请求返回参数")
public class ApiResponse<T> {

    public static final int SUCCESS_CODE = 0;
    public static final int ERROR_CODE = 100;

    private Integer code;
    private String message;
    private T data;

    public ApiResponse() {
        this.code = SUCCESS_CODE;
        this.message = "";
    }

    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> successOf(T data) {
        return new ApiResponse<>(SUCCESS_CODE, "", data);
    }

    public static <T> ApiResponse<T> failOf(String message) {
        return new ApiResponse<>(ERROR_CODE, message, null);
    }
}
