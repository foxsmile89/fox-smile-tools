package com.smile.tools.lambda;

import com.smile.tools.dto.LoanDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * lambda集合收集自测
 * @author smile
 * @date 2019-07-08 10:50:00
 * @version 1.0
 **/
public class LambdaCollect {

    private static final Logger logger = LoggerFactory.getLogger(LambdaCollect.class);

    public static void main(String[] args){

        List<LoanDto> loanList = new ArrayList<>();
        LoanDto loanDto1 = new LoanDto();
        loanDto1.setLoanId(1001L);
        loanDto1.setUserId("abc");
        loanDto1.setLoanAmt(new BigDecimal("100"));

        LoanDto loanDto2 = new LoanDto();
        loanDto2.setLoanId(1002L);
        loanDto2.setUserId("cde");
        loanDto2.setLoanAmt(new BigDecimal("200"));

        loanList.add(loanDto1);
        loanList.add(loanDto2);

        Map<String,List<LoanDto>> mapByUser = loanList.stream().collect(Collectors.groupingBy(LoanDto::getUserId));
        List<BigDecimal> loanAmtList = loanList.stream().map(LoanDto::getLoanAmt).collect(Collectors.toList());

        logger.info("loanList before:{}", loanList);

        //分组map更改list值后,【原list中的对象值变更且原对象值也变更】!!!!
        mapByUser.values().forEach(userLoanList -> userLoanList.forEach(loanDto -> loanDto.setLoanAmt(loanDto.getLoanAmt().add(new BigDecimal("123")))));
        logger.info("loanList after1:{}", loanList);

        logger.info("loanList after1 loan1:{}, loan2:{}", loanDto1, loanDto2);

        //map过滤list更改值后
        for (int i=0; i<loanAmtList.size(); i++) {
            loanAmtList.set(i,loanAmtList.get(i).add(new BigDecimal("111")));
        }
        logger.info("loanList after2:{}", loanList);
    }

}
