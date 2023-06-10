package com.innovation.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author LiuYan
 * @version 1.0
 * @className TransactionIdGenerateUtil
 * @description 交易流水号生成工具
 * @date 2021/9/7 16:00
 */
@Component
public class TransactionIdGenerateUtil {
    private long wordId = 0; // 本机终端id
    private long dataCenterId = 1; // 数据中心id
    private Snowflake snowflake = IdUtil.createSnowflake(wordId, dataCenterId); // 雪花算法id生成器对象

    /**
     * @description 对象加载完依赖注入后执行，初始化参数，且只执行一次
     */
    @PostConstruct
    public void init() {
        wordId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
    }

    /**
     * @description 获取交易流水号
     * @return transactionId 由雪花算法在默认本机终端id和数据中心id下生成的交易流水号
     */
    public long getTransactionId() {
        long transactionId = snowflake.nextId();
        return transactionId;
    }

    /**
     * @param wordId       机器终端id
     * @param dataCenterId 数据中心id
     * @return transactionId 由雪花算法在指定本机终端id和数据中心id下生成的交易流水号
     */
    public long getTransactionId(long wordId, long dataCenterId) {
        Snowflake snowflake = IdUtil.createSnowflake(wordId, dataCenterId); // 雪花算法id生成器对象
        long transactionId = snowflake.nextId();
        return transactionId;
    }

}
