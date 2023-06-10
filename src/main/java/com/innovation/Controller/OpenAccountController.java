package com.innovation.Controller;

import com.innovation.Service.OpenAccountService;
import com.innovation.entity.ResponseBean;
import com.innovation.vo.OpenAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 在银行柜面进行开户， 1.查询用户信息，判断用户是否开户，如果已经开户，告知用户已开户 customer_personal_information
 * 2.如果没有用户信息，输入用户姓名、证件类型、证件号码、证件有效期、国家/地区、地址、手机号、邮箱、交易密码 3.生成活期存款账户
 * 4.是否开通网上银行，如果开通，生成初始登录密码
 */
@CrossOrigin
@RestController
@RequestMapping("Open")
public class OpenAccountController {
    @Autowired
    private OpenAccountService openAccountService;

    /**
     * 前端接收客户的姓名、证件类型、证件号码、证件有效期、国家/地区、地址、手机号、邮箱、交易密码
     * 开户成功的话，返回的状态码为1（表示开户成功），返回的msg为银行卡密码，返回数据为初始登录密码
     * 开户失败的话，返回状态码为0（表示开户失败），返回信息为“客户已经开户，无需重复开户”，返回数据为空
     */
    @PostMapping("OpenAccount")
    public ResponseBean OpenAccount(@RequestBody OpenAccount openAccount) {
        ResponseBean responseBean = openAccountService.openAccount(openAccount);
        return responseBean;

    }

}
