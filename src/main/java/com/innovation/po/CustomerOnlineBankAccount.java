package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CustomerOnlineBankAccount {
    private int id;
    private String idNumber;//身份证号
    private String loginPassword;//登录密码
    private String phoneNumber;//手机号
    private String loginStatus;//登录状态
    private String lastLoginTime;//上次登录时间

}
