package com.innovation.Controller;

import com.innovation.Service.CustomerTransactionService;
import com.innovation.vo.AccountShowingInformation;
import com.innovation.vo.CustomerTransferData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CustomerTransactionControllerTest {
    @Autowired
    private CustomerTransactionService customerTransactionService;
    @Test
    void getAccountShowingInformation() {
        String idNumber = "130602";
        AccountShowingInformation accountShowingInformation = customerTransactionService.getAccountShowingInformation(idNumber);
        System.out.println(accountShowingInformation.toString());
    }

    @Test
    void transferRequestFeedback() {
//        CustomerTransferData customerTransferData = new CustomerTransferData("6212262201023557228","6212262201023557229","中国建设银行","实时转账",new BigDecimal(100),"123456");
//
//        System.out.println(customerTransactionService.transferRequestFeedback(customerTransferData));
    }

}