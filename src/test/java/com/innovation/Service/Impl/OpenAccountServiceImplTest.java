package com.innovation.Service.Impl;

import com.innovation.Dao.OpenAccountMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class OpenAccountServiceImplTest {
    @Autowired
    private OpenAccountMapper openAccountMapper;
    @Test
    void findCustomerByIDNumber() {
        int x=  openAccountMapper.findCustomerByIDNumber("130602");
    }
}