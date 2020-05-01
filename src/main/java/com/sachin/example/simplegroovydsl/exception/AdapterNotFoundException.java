package com.sachin.example.simplegroovydsl.exception;

public class AdapterNotFoundException extends RuntimeException {

    public AdapterNotFoundException() {
        super();
    }

    public AdapterNotFoundException(String name) {
        super("找不到对应的DSL执行器: " + name);
    }

}
