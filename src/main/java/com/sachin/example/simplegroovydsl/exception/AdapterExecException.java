package com.sachin.example.simplegroovydsl.exception;

public class AdapterExecException extends RuntimeException {


    public AdapterExecException() {
        super();
    }

    public AdapterExecException(String name) {
        super("执行DSL失败: " + name);
    }

    public AdapterExecException(String name, String message) {
        super("执行DSL失败: " + name + ". message: " + message);
    }

    public AdapterExecException(String name, Throwable e) {
        super("执行DSL失败: " + name, e);
    }

    public AdapterExecException(String name, String message, Throwable e) {
        super("执行DSL失败: " + name + ". message: " + message, e);
    }

}
