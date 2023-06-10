package com.innovation.po;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Data
@Component
public class InterestRateDefinition {
    private int id;
    private String interestRateCode;//利率代码
    private String currency;//币种
    private String effectiveDate;//生效日期
    private String failureDate;//失效日期
    private String interestRateType;//利率类型
    private String benchmark;//基准天数
    private BigDecimal benchmarkInterestRate;//基准利率
    private String updated;//更新日期
    private String updateOperator;//更新操作员
    private String remark;//备注
    private String settlementDay;

}
