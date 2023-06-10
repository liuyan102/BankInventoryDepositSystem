package com.innovation.Dao;

import org.springframework.stereotype.Repository;

@Repository
public interface TellerLoginMapper {

    Integer findTellerByTellerName(String tellerName);

    String findPwdByTellerName(String tellerName);

    Integer updateStatus(String tellerName, String tellerStatus);
}
