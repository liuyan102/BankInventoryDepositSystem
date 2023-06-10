package com.innovation.entity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionResponse {
    @ExceptionHandler(OperationException.class)
    @ResponseBody
    public Map<String,Object> prodProcessException(OperationException e){
        Map<String,Object> modelMap = new HashMap<>();
        modelMap.put("success",false);
        modelMap.put("errorMsg",e.getMsg());
        return modelMap;
    }
}
