package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Subject {
    private int id;
    private String glCode;
    private String glName;
    private String scope;
}
