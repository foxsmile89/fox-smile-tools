package com.smile.tools.lambda;

import com.smile.tools.dto.LoanDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
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
        loanDto1.setPlanNum(1);
        loanDto1.setLoanAmt(new BigDecimal("100"));

        LoanDto loanDto2 = new LoanDto();
        loanDto2.setLoanId(1002L);
        loanDto2.setUserId("cde");
        loanDto2.setPlanNum(3);
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

        //------------------------------------------------------------------------------------------------
        long count = loanList.stream().collect(Collectors.counting());
        count = loanList.stream().count();
        logger.info("1 count:{}",count);

        Optional<LoanDto> maxLoan = loanList.stream().collect(Collectors.maxBy((a, b) -> a.getPlanNum().compareTo(b.getPlanNum())));
        maxLoan = loanList.stream().max(Comparator.comparing(LoanDto::getPlanNum));
        maxLoan = loanList.stream().max(Comparator.comparingInt(LoanDto::getPlanNum));
        maxLoan = loanList.stream().max(Comparator.comparing(loanDto -> loanDto.getPlanNum().compareTo(1)));

        LoanDto mLoan = null;
        //判断对象是否为null
        if (maxLoan.isPresent()){
            mLoan = maxLoan.get();
        }
        logger.info("1 mLoan:{}", mLoan);

        //如果存在元素,则将元素属性变更
        maxLoan.ifPresent(loanDto -> loanDto.setLoanAmt(loanDto.getLoanAmt().add(new BigDecimal("123"))));
        mLoan = maxLoan.get();
        logger.info("2 mLoan:{}", mLoan);

        //汇总操作,被操作的参数类型必须与方法要求一致,不一致时需转换成所需类型
        int sumInt = loanList.stream().collect(Collectors.summingInt(LoanDto::getPlanNum));
        double sumDb = loanList.stream().collect(Collectors.summingDouble(loan->loan.getLoanAmt().doubleValue()));
        logger.info("1 sumInt:{},sumDb:{}", sumInt, sumDb);

        sumInt = loanList.stream().mapToInt(LoanDto::getPlanNum).sum();
        sumDb = loanList.stream().mapToDouble(loan->loan.getLoanAmt().doubleValue()).sum();
        logger.info("2 sumInt:{},sumDb:{}", sumInt, sumDb);

        double avgInt = loanList.stream().collect(Collectors.averagingInt(LoanDto::getPlanNum));
        double avgDb = loanList.stream().collect(Collectors.averagingDouble(loan->loan.getLoanAmt().doubleValue()));
        logger.info("1 avgInt:{},avgDb:{}", avgInt, avgDb);

        //mapToInt的average只能返回OptionalDouble结果
        OptionalDouble avgIntOpDb = loanList.stream().mapToInt(LoanDto::getPlanNum).average();
        OptionalDouble avgDbOpDb = loanList.stream().mapToDouble(loan->loan.getLoanAmt().doubleValue()).average();
        if (avgIntOpDb.isPresent() && avgDbOpDb.isPresent()){
            logger.info("2 avgInt:{},avgDb:{}", avgIntOpDb.getAsDouble(), avgDbOpDb.getAsDouble());
        }

        //findFirst返回首记录结果
        OptionalInt opInt = loanList.stream().mapToInt(LoanDto::getPlanNum).findFirst();
        if (opInt.isPresent()){
            logger.info("3 firstInt:{}", opInt.getAsInt());
        }

        //一次性得到常用统计项
        IntSummaryStatistics intStatis = loanList.stream().collect(Collectors.summarizingInt(LoanDto::getPlanNum));
        DoubleSummaryStatistics dbStatis = loanList.stream().collect(Collectors.summarizingDouble(loan->loan.getLoanAmt().doubleValue()));
        logger.info("1 intStatis:{}, dbStatis:{}", intStatis, dbStatis);
    }

}
