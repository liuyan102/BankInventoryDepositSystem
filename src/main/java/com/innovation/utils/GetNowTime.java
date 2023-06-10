package com.innovation.utils;

import com.innovation.Dao.SystemSettingMapper;

import com.innovation.config.ApplicationContextProvider;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetNowTime {

    private SystemSettingMapper systemSettingMapper;

    public String getRealityTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(calendar.getTime());
    }

    public String getNowTime() {
        this.systemSettingMapper = ApplicationContextProvider.getBean(SystemSettingMapper.class);
        String[] x = getRealityTime().split(" ");
        return systemSettingMapper.getSystemTime() + " " + x[1];
    }

    public String getNowDate() {
        this.systemSettingMapper = ApplicationContextProvider.getBean(SystemSettingMapper.class);
        return systemSettingMapper.getSystemTime();
    }

    public String getThisDate(String date) {
        String[] result = date.split("T", 2);
        return result[0];
    }

    public String getNextDate(String date) {
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date temp = dft.parse(date);
            Calendar cld = Calendar.getInstance();
            cld.setTime(temp);
            cld.add(Calendar.DATE, 1);
            temp = cld.getTime();
            // 获得下一天日期字符串
            String nextDay = dft.format(temp);
            return nextDay;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
