package com.innovation.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class getCron {
    /***
     *
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /***
     * convert Date to cron ,eg. "0 06 10 15 1 ? 2014"
     * 
     * @param date : 时间点
     * @return
     */
    public static String getCron(java.util.Date date) {
        String dateFormat = "ss mm HH * * ?";
        return formatDateByPattern(date, dateFormat);
    }

    public static String getCronData(java.util.Date date) {
        String dateFormat = "ss mm HH * * ?";
        return formatDateByPattern(date, dateFormat);
    }
}
