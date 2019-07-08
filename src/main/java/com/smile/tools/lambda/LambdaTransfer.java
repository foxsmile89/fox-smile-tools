package com.smile.tools.lambda;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.smile.tools.dto.LoanDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LambdaTransfer {

    private static final Logger logger = LoggerFactory.getLogger(LambdaTransfer.class);

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

        logger.info("loanList before:{}", loanList);

        //过滤无用字段
        //匿名内部类写法
        String loanStr1 = JSON.toJSONString(loanList, new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                return ("loanId".equals(name) || "userId".equals(name));
            }
        });

        //lambda表达式写法
        PropertyFilter pf = (Object object, String name, Object value) -> ("loanId".equals(name) || "userId".equals(name));
        String loanStr2 = JSON.toJSONString(loanList, pf);

        logger.info("loanStr1 after:{}", loanStr1);
        logger.info("---------------------------------------------------");
        logger.info("loanStr2 after:{}", loanStr2);
    }

}
