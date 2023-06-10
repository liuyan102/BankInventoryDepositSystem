package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CustomerCurrentSavingsAccount {
    private int id;
    private String idNumber;//身份证号
    private String externalAccount;//银行卡号
}
