package com.innovation.Service;

import com.innovation.entity.ResponseBean;
import com.innovation.vo.InformationCorrect;

public interface LoginService {

    ResponseBean login(String type, String pwd, String idnumber, String mobilePhoneNumber, String verificationCode);

    String getVerificationCode(String mobilePhoneNumber);

    String findIdNumByPho(String phone);

    Integer updateInformation(InformationCorrect informationCorrect);

    InformationCorrect getInfo(String idNumber);

    String updateLoginPwd(String oldPwd,String newPassword,String idNumber);
}
