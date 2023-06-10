package com.innovation.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ResponseBean {
    /**
     * HTTP状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回的数据
     */
    private Object data;

}
