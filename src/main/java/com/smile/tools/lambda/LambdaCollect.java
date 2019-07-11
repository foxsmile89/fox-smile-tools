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
        loanDto1.setUserId("afc");
        loanDto1.setPlanNum(1);
        loanDto1.setLoanAmt(new BigDecimal("100"));

        LoanDto loanDto2 = new LoanDto();
        loanDto2.setLoanId(1002L);
        loanDto2.setUserId("ade");
        loanDto2.setPlanNum(3);
        loanDto2.setLoanAmt(new BigDecimal("200"));

        loanList.add(loanDto1);
        loanList.add(loanDto2);
        logger.info("loanList before:{}", loanList);

        Map<String,List<LoanDto>> group1 = loanList.stream().collect(Collectors.groupingBy(LoanDto::getUserId));
        List<BigDecimal> loanAmtList = loanList.stream().map(LoanDto::getLoanAmt).collect(Collectors.toList());
        //二级分组,先按userId,再按loanId分组
        Map<String,Map<Long,List<LoanDto>>> group2 = loanList.stream().collect(Collectors.groupingBy(LoanDto::getUserId, Collectors.groupingBy(LoanDto::getLoanId)));
        logger.info("group2:{}", group2);

        //分组统计
        Map<String,Long> groupCount = loanList.stream().collect(Collectors.groupingBy(LoanDto::getUserId, Collectors.counting()));
        logger.info("groupCount:{}", groupCount);

        //userId作为key, 将对象的loanId过滤为set后作为value，返回时map.key乱序
        Map<String,Set<Long>> groupSet = loanList.stream().collect(Collectors.groupingBy(LoanDto::getUserId, Collectors.mapping(LoanDto::getLoanId, Collectors.toSet())));
        logger.info("groupSet:{}", groupSet);

        //第2个参数,指定生成的map按顺序存放的map,返回时按map存放顺序
        Map<String,Set<Long>> groupLinkMapSet = loanList.stream().collect(Collectors.groupingBy(LoanDto::getUserId,
                LinkedHashMap::new,
                Collectors.mapping(LoanDto::getLoanId, Collectors.toSet())));
        logger.info("groupLinkMapSet:{}", groupLinkMapSet);

        //groupingBy分组为map更改list值后,【原list中的对象值变更且原对象值也变更】!!!!
        group1.values().forEach(userLoanList -> userLoanList.forEach(loanDto -> loanDto.setLoanAmt(loanDto.getLoanAmt().add(new BigDecimal("123")))));
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


        //转换为set,并验证是否深复制,不是深复制，会更改原集合元素值和对象原值
        Set<LoanDto> loanSet = loanList.stream().collect(Collectors.toSet());
        //替代写法
        loanSet = new HashSet<>(loanList);
        loanSet.forEach(loanDto -> loanDto.setLoanAmt(loanDto.getLoanAmt().add(new BigDecimal("111"))));
        logger.info("toSet modify after,loanList:{},loan1:{}", loanList, loanDto1);

        //拼接元素,要求调用的list元素值必须为String类型,否则报错
        List<String> userList = loanList.stream().map(LoanDto::getUserId).collect(Collectors.toList());
        String join1 = userList.stream().collect(Collectors.joining());
        //第1个参数为元素间分割符, 2为整体前缀, 3为整体后缀
        String join2 = userList.stream().collect(Collectors.joining("-|-","[","]"));
        logger.info("join1:{},,,,join2:{}", join1,join2);

        //partitioningBy验证
        //loanList.stream().collect(Collectors.partitioningBy(LoanDto))
    }

}
