package com.ocean.exception;

/**
 * 自定义异常
 */
public class CustomerErrorException extends RuntimeException {
    public CustomerErrorException(){}

    public CustomerErrorException(String message){
        super(message);
    }
}
