package com.innovation.Controller;

import com.innovation.Service.LoginService;
import com.innovation.entity.ResponseBean;
import com.innovation.vo.InformationCorrect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
public class LoginController {
    @Autowired
    private ResponseBean responseBean;
    @Autowired
    private LoginService loginService;

    /**
     * getInfo
     * 根据身份证号，将个人信息表所有数据（在customer_personal_information表中）+手机号（在customer_online_bank_account表中）+银行账号(在customer_current_savings_account表中)返回给前端
     * 将获取到的信息全部封装到InformationCorrect中第一个对象中
     */
    @RequestMapping("/getInfo")
    public InformationCorrect getInfo(@RequestBody Map<String, String> map) {
        String idNumber = map.get("idNumber");
        // 根据身份证号，将个人信息表所有数据+银行账号返回给前端
        InformationCorrect info = loginService.getInfo(idNumber);
        return info;
    }

    /**
     * 获取前端传过来的姓名、职业、家庭固定电话、单位固定电话、住所地、工作单位地址 身份证有效期，
     * 将信息添加到customer_personal_information表中
     *
     * @param
     * @return
     */
    @RequestMapping("/correct")
    public ResponseBean correctInformation(@RequestBody InformationCorrect informationCorrect) {

        Integer i = loginService.updateInformation(informationCorrect);// 补充信息
        responseBean.setMsg("修改密码成功");
        responseBean.setCode(1);
        return responseBean;
    }

    /**
     * 通过身份证号+密码 或 手机号+验证码进行登录 对前端接收来的数据要进行非空判断
     */
    @RequestMapping("/login")
    public ResponseBean login(@RequestBody Map<String, String> map) {

        String type = map.get("type");// type为1表示身份证+密码进行登录，为2表示手机号+验证码进行登录
        String pwd = map.get("loginPassword");
        String idnumber = map.get("documentNumber");
        String mobilePhoneNumber = map.get("mobilePhoneNumber");
        String verificationCode = map.get("verificationCode");
        responseBean = loginService.login(type, pwd, idnumber, mobilePhoneNumber, verificationCode);
        return responseBean;
    }

    /**
     * 获取验证码
     * 
     * @return
     */
    @RequestMapping("/getVerificationCode")
    public ResponseBean getVerificationCode(@RequestBody Map<String, String> map) {

        String mobilePhoneNumber = map.get("mobilePhoneNumber");
        String code = loginService.getVerificationCode(mobilePhoneNumber);
        responseBean.setMsg("发送验证码成功");
        responseBean.setData(code);
        responseBean.setCode(1);
        return responseBean;

    }

    @RequestMapping("/getIdNumber")
    public Map<String, Object> getBankNumber(@RequestBody Map<String, Object> hasMap) {
        String tel = hasMap.get("mobilePhoneNumber").toString();
        hasMap.put("idNumber", loginService.findIdNumByPho(tel));
        return hasMap;
    }

    @RequestMapping("/updateLoginPwd")
    public Map<String,String> updateLoginPwd(@RequestBody Map<String,String> hasMap){
        String oldPwd = hasMap.get("oldLoginPwd");
        String newPassword = hasMap.get("NewLoginPwd");
        String idNumber = hasMap.get("idNumber");
        String msg = loginService.updateLoginPwd(oldPwd,newPassword,idNumber);
        if(msg.equals("修改成功")){
            hasMap.put("success","true");
        }else{
            hasMap.put("success","false");
        }
        hasMap.put("msg",msg);
        return hasMap;
    }

}
