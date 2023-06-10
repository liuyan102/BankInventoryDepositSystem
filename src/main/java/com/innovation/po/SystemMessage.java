package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
@Data
@Component
public class SystemMessage {
    private int id;
    private String systemNumber;
    private String systemName;
    private String currentSystemDate;
    private String lastDay;
    private String nextDay;
    private String complimentaryTime;
    private String comparativeState;
    private String settlementDay;
}
