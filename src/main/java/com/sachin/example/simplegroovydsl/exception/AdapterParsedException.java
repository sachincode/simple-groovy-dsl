package com.sachin.example.simplegroovydsl.exception;

public class AdapterParsedException extends RuntimeException {

    public AdapterParsedException() {
        super();
    }

    public AdapterParsedException(String name, Throwable e) {
        super("解析编译DSL失败: " + name, e);
    }

}
