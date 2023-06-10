package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class BankCashBalance {
    private int id;
    private String branch;
    private String tellerId;
    private BigDecimal balance;

    @Override
    public String toString() {
        return "BankCashBalance{" +
                "id=" + id +
                ", branch='" + branch + '\'' +
                ", tellerId='" + tellerId + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTellerId() {
        return tellerId;
    }

    public void setTellerId(String tellerId) {
        this.tellerId = tellerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
