package com.smile.tools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 贷款单dto
 * @author smile
 * @date 2019-07-08 10:50:00
 * @version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDto implements Serializable {

    private static final long serialVersionUID = -7513050558177961797L;

    /**
     * 贷款单号
     */
    private Long loanId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 分期数
     */
    private Integer planNum;

    /**
     * 贷款金额
     */
    private BigDecimal loanAmt;

}
