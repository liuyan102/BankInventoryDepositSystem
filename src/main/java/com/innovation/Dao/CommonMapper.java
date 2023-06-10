package com.innovation.Dao;

import com.innovation.po.CurrentSavingsAccountBalance;
import com.innovation.po.CustomerTradingWater;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
@Repository
@Mapper
public interface CommonMapper {

    //�������п����ӿڲ���Ϊ���п��ź��������������
    int updatebankbalance(String external_account,BigDecimal changebalance);

    //�ڿͻ�������ˮ���в���һ����ˮ���ݣ�����Ϊ�ͻ�������ˮ����
    int inserttradingwater(CustomerTradingWater customerTradingWater,@Param("object") int object);

    //��ѯ�ͻ����ڴ����˻��������������������
    CurrentSavingsAccountBalance queryAccountBalanceInformation(String externalAccount);

    //�������п��Ż�ȡ����
    String getAccountName(String externalAccount);

    //�������п��Ż�ȡ�����˻�
    String getAccountNameByCardNumber(String bankCardNumber);

}
