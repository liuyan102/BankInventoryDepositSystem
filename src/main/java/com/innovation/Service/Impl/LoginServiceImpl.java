package com.innovation.Service.Impl;

import com.innovation.Dao.LoginMapper;
import com.innovation.Dao.SystemSettingMapper;
import com.innovation.Service.LoginService;
import com.innovation.entity.OperationException;
import com.innovation.entity.ResponseBean;
import com.innovation.vo.InformationCorrect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    LoginMapper loginMapper;
    @Autowired
    ResponseBean responseBean;
    @Autowired
    private SystemSettingMapper systemSettingMapper;
    public String verificationCode1 = "";
    public static Map<String, String> phoneVerificationCode = new HashMap<>();

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResponseBean login(String type, String pwd, String idnumber, String mobilePhoneNumber,
            String verificationCode) {

        try {

            /**
             * 按照身份证号+密码登录
             */
            if (type.equals("1")) {// 按照身份证号+密码登录
                Integer customerByIdNumber = loginMapper.findCustomerByIdNumber(idnumber);

                if (customerByIdNumber == 0) {
                    responseBean.setCode(0);
                    responseBean.setMsg("您所输入的身份信息尚未开通网上银行");
                    return responseBean;
                } else if (customerByIdNumber == 1) {

                    String md5Pass = DigestUtils.md5DigestAsHex(pwd.getBytes());
                    String pwdByIdnumber = loginMapper.findPwdByIdnumber(idnumber);
                    if (pwdByIdnumber.equals(md5Pass)) { // 判断密码是否正确
                        // 判断激活状态，如果是未激活，说明是初次登录，修改状态为激活状态
                        if (loginMapper.findActiveByIdNumber(idnumber).equals("未激活")) {
                            responseBean.setCode(2);
                            responseBean.setMsg("本次是初次登录，请跳转到修改密码页面");
                            return responseBean;
                        } else if (loginMapper.findActiveByIdNumber(idnumber).equals("已激活")) {
                            // 记录上次登录时间
                            String lastLoginTime = loginMapper.findLastLoginTime(idnumber);
                            System.out.println(lastLoginTime);
                            // 更新本次登录时间
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date(System.currentTimeMillis());
                            Integer x = loginMapper.updateLoginTime(formatter.format(date), idnumber);
                            // 登录状态为已登录
                            loginMapper.updateStatus(idnumber, "已登录");
                            responseBean.setCode(1);
                            responseBean.setMsg("请跳转到主页面");
                            return responseBean;
                        }

                    } else {
                        responseBean.setMsg("密码不正确");
                        responseBean.setCode(0);
                        return responseBean;
                    }
                }
            }

            /**
             * 按照手机号+验证码登录
             */
            if (type.equals("2")) {
                // 判断手机号在customer_online_bank_account表中是否存在
                String s = loginMapper.findIdNumberByPhoneNumber(mobilePhoneNumber);
                if (s == null||s=="") {// 如果不存在
                    responseBean.setCode(0);
                    responseBean.setMsg("您好,您所输入的手机号尚未开通网上银行");
                    return responseBean;
                } else {

                    // 判断输入的验证码与后端生成的验证码是否是一样的
                    if (verificationCode.equals(verificationCode1)) {
                        String idNumberByPhoneNumber = loginMapper.findIdNumberByPhoneNumber(mobilePhoneNumber);
                        if (loginMapper.findActiveByIdNumber(idNumberByPhoneNumber).equals("未激活")) {
                            responseBean.setCode(2);
                            responseBean.setMsg("请跳转到修改密码页面");
                            return responseBean;
                        } else if (loginMapper.findActiveByIdNumber(idNumberByPhoneNumber).equals("已激活")) {
                            // 记录上次登录时间
                            String lastLoginTime = loginMapper.findLastLoginTime(idNumberByPhoneNumber);
                            System.out.println(lastLoginTime);
                            // 更新本次登录时间
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date(System.currentTimeMillis());
                            Integer x = loginMapper.updateLoginTime(formatter.format(date), idNumberByPhoneNumber);
                            // 登录状态为已登录
                            loginMapper.updateStatus(idNumberByPhoneNumber, "已登录");
                            responseBean.setCode(1);
                            responseBean.setMsg("请跳转到主页面");
                            return responseBean;
                        }
                    } else {
                        responseBean.setCode(0);
                        responseBean.setMsg("验证码输入不正确，请重新输入");
                        return responseBean;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseBean;
        }
        return responseBean;
    }

    @Override
    public String getVerificationCode(String mobilePhoneNumber) {
        String[] numbers = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

        String[] Number = new String[6];
        Random rand = new Random();
        verificationCode1 = "";
        for (int i = 0; i < 6; i++) {
            Number[i] = numbers[rand.nextInt(10)];
            verificationCode1 += Number[i];
        }

        phoneVerificationCode.put(mobilePhoneNumber, verificationCode1);
        String s = phoneVerificationCode.get(mobilePhoneNumber);
        System.out.println(s);
        phoneVerificationCode.remove(mobilePhoneNumber);
        return s;
    }

    @Override
    public Integer updateInformation(InformationCorrect informationCorrect) {

        String idnumber = informationCorrect.getIdnumber();// 身份证号
        String occupation = informationCorrect.getProfession();// 职业
        String familyLine = informationCorrect.getFamilyFixedPhone();// 家庭电话
        String workplaceLine = informationCorrect.getUnitFixationPhone();// 单位电话
        String newPass = informationCorrect.getLoginPwd();// 新密码
        String workplace = informationCorrect.getWorkUnitAddress();// 工作地点

        Integer result = loginMapper.updateInformation(idnumber,informationCorrect.getMail(),
                occupation, familyLine, workplaceLine, workplace);
        if(result==0){
            throw new OperationException("0027",systemSettingMapper.getInformation("0027"));
        }

        if(newPass!=null){
            String newPassword = DigestUtils.md5DigestAsHex(newPass.getBytes());// 对输入的密码进行加密
            int result1 = loginMapper.updateLoginPwd(newPassword,idnumber);
            if(result1==0){
                throw new OperationException("0036",systemSettingMapper.getInformation("0036"));
            }
        }

        String active = "已激活";
        Integer flag2 = loginMapper.updateActiveByIdNumber(idnumber, active);

        if (flag2 == 0) {
            throw new OperationException("4008", systemSettingMapper.getInformation("4008"));
        }
        // 记录登录时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        Integer x = loginMapper.updateLoginTime(formatter.format(date), idnumber);
        // 登录状态为已登录
        loginMapper.updateStatus(idnumber, "已登录");
        responseBean.setCode(1);

        return 1;
    }

    @Override
    public InformationCorrect getInfo(String idNumber) {
        InformationCorrect info = loginMapper.getInfo(idNumber);
        return info;
    }

    @Override
    public String findIdNumByPho(String phone) {
        String idNumberByPhoneNumber = loginMapper.findIdNumberByPhoneNumber(phone);
        return idNumberByPhoneNumber;
    }

    @Override
    public String updateLoginPwd(String oldPwd,String newPwd,String idnumber){
        String oldPassword = DigestUtils.md5DigestAsHex(oldPwd.getBytes());// 对输入的密码进行加密
        String newPassword = DigestUtils.md5DigestAsHex(newPwd.getBytes());
        String pwdByIdnumber = loginMapper.findPwdByIdnumber(idnumber);
        int result=0;
        String msg="";
        if(oldPassword.equals(pwdByIdnumber)){
            result = loginMapper.updateLoginPwd(newPassword,idnumber);
            msg = "修改成功";
        }else{
            msg = "原密码错误";
        }
        return msg;
    }

}
