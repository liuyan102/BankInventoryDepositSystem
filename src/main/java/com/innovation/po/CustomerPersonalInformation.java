package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CustomerPersonalInformation {
    private int id;
    private String name;//姓名
    private String typeOfCertificate;//证件类型
    private String idNumber;//证件号码
    private String certificateValidityPeriod;//证件有效期
    private String countryRegion;//国家/地区
    private String address;//居住地址
    private String mail;//邮箱

}
