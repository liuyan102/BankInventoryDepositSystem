package com.innovation.entity;

import lombok.Data;

@Data
public class OperationException extends RuntimeException{
    private String status;
    private String msg;
    public OperationException(String status,String msg){
        this.status = status;
        this.msg = msg;
    }
}
