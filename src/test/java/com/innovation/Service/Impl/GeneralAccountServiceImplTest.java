package com.innovation.Service.Impl;

import com.innovation.Application;
import com.innovation.Dao.GeneralAccountMapper;
import com.innovation.po.GeneralAccount;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class GeneralAccountServiceImplTest {

    @Autowired
    GeneralAccountMapper generalAccountMapper;

    @Test
    void totalAccountFlowInquiry() {
//        List<GeneralAccount> list = generalAccountMapper.totalAccountFlowInquiry("现金总账");
//        System.out.println(list);
    }
}