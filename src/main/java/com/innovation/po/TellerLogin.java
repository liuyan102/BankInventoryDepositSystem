package com.innovation.po;

import org.springframework.stereotype.Component;

@Component
public class TellerLogin {
   private Integer id;
    private String tellerId;
    private String tellerName;
    private String  tellerPwd;
    private String tellerStatus;

    public TellerLogin() {
    }

    public TellerLogin(Integer id, String tellerId, String tellerName, String tellerPwd, String tellerStatus) {
        this.id = id;
        this.tellerId = tellerId;
        this.tellerName = tellerName;
        this.tellerPwd = tellerPwd;
        this.tellerStatus = tellerStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTellerId() {
        return tellerId;
    }

    public void setTellerId(String tellerId) {
        this.tellerId = tellerId;
    }

    public String getTellerName() {
        return tellerName;
    }

    public void setTellerName(String tellerName) {
        this.tellerName = tellerName;
    }

    public String getTellerPwd() {
        return tellerPwd;
    }

    public void setTellerPwd(String tellerPwd) {
        this.tellerPwd = tellerPwd;
    }

    public String getTellerStatus() {
        return tellerStatus;
    }

    public void setTellerStatus(String tellerStatus) {
        this.tellerStatus = tellerStatus;
    }
}
