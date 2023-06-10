package com.innovation.vo;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class InformationCorrect {
    // 初次登录完善用户信息
    private String name;// 用户姓名
    private String externalAccount;// 银行卡号
    private String typeOfCertificate;// 证件类型
    private String idnumber;// 身份证号
    private String phoneNumber;// 手机号
    private String address;// 居住地址
    private String mail;
    private String certificateStartPeriod;// 证件开始时间
    private String certificateEndPeriod;// 证件有效期结束时间
    private String countryRegion;// 国家地区
    private String profession;// 职业信息
    private String familyFixedPhone;// 家庭固定电话
    private String unitFixationPhone;// 单位固定电话
    private String workUnitAddress;// 工作地点
    private String loginPwd;// 新密码
    private List<String> idCardValidity;// 证件有效期
}
