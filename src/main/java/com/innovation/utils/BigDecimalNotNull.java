package com.innovation.utils;

import java.math.BigDecimal;

/**
 * @author LiuYan
 * @version 1.0
 * @className BigDecimalNotNull
 * @description BigDecimal不为空
 * @date 2021/9/23 9:25
 */
public class BigDecimalNotNull {
    public static BigDecimal bigDecimalNotNUll(BigDecimal bigDecimal){
        if(bigDecimal == null)
            bigDecimal = new BigDecimal(0);
        return bigDecimal;
    }
}
