package com.innovation.Dao;

import com.innovation.po.CustomerOnlineBankAccount;
import com.innovation.vo.InformationCorrect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginMapper {

    Integer findCustomerByIdNumber(String idNumber);

    String findPwdByIdnumber(String idnumber);

    void updateStatus(@Param("idNumber") String idNumber,@Param("loginStatus") String loginStatus);

    String findLastLoginTime(String idnumber);

    Integer updateLoginTime(@Param("lastLoginTime") String lastLoginTime,@Param("idNumber") String idNumber);

    String findActiveByIdNumber(String idNumber);

    Integer updateActiveByIdNumber(String idNumber,String active);

    String findIdNumberByPhoneNumber(String phoneNumber);

    Integer updateInformation(String idNumber,String mail, String occupation,String familyLine,String workplaceLine,String workplace);

    InformationCorrect getInfo(String idNumber);

    int updateLoginPwd(String newPassword,String idNumber);


}
