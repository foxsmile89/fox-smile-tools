package com.smile.tools.lambda;

import com.smile.tools.dto.LoanDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
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

        LoanDto loanDto3 = new LoanDto();
        loanDto3.setLoanId(1003L);
        loanDto3.setUserId("ade");
        loanDto3.setPlanNum(3);
        loanDto3.setLoanAmt(new BigDecimal("300"));

        loanList.add(loanDto1);
        loanList.add(loanDto2);
        loanList.add(loanDto3);
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

        //3参分组,第2个参数,指定生成的map按顺序存放的map,返回时按map存放顺序
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

        //转换为并发map,key=String,value=Set<BigDecimal>
        ConcurrentMap<String,Set<BigDecimal>> cMapGroup1 = loanList.stream().collect(
                Collectors.groupingByConcurrent(LoanDto::getUserId,
                Collectors.mapping(LoanDto::getLoanAmt, Collectors.toSet())));
        logger.info("cMapGroup1:{}",cMapGroup1);

        //转换为并发Map,key=String,value=Set<LoanDto>
        ConcurrentSkipListMap<String,Set<LoanDto>> cMapGroup2 = loanList.stream().collect(
                Collectors.groupingByConcurrent(LoanDto::getUserId,
                        ConcurrentSkipListMap::new,
                        Collectors.toSet()));
        logger.info("cMapGroup2:{}",cMapGroup2);
        //------------------------------------------------------------------------------------------------
        long count = loanList.stream().collect(Collectors.counting());
        //替代写法
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

        //partitioningBy验证,根据分期数分组成是否map,一组key为true,一组key为false
        //分组的参数条件为一个能返回是否的表达式
        Map<Boolean,List<LoanDto>> partMap1 = loanList.stream().collect(
                Collectors.partitioningBy(loan->loan.getPlanNum().compareTo(3)>=0));
        logger.info("partMap1:{}", partMap1);

        //分组为是否map, value域为Set<BigDecimal>仅为过滤的金额字段
        Map<Boolean,Set<BigDecimal>> partMap2 = loanList.stream().collect(Collectors.partitioningBy(
                loan->loan.getPlanNum().compareTo(3)>=0,
                Collectors.mapping(LoanDto::getLoanAmt, Collectors.toSet())));
        logger.info("partMap2:{}", partMap2);

        //将list转换为map,key为对象属性,value可为属性,也可为根据对象构建的新对象
        //若作为key的属性重复,会抛出键值冲突异常,可用下方方法替换,由新值覆盖旧值
        Map<String,LoanDto> mapDto = null;
        try{
            mapDto = loanList.stream().collect(Collectors.toMap(LoanDto::getUserId,
                    loanDto->LoanDto.builder()
                            .loanAmt(loanDto.getLoanAmt())
                            .planNum(loanDto.getPlanNum())
                            .loanId(1L).build()
            ));
        } catch (Exception ignore){
            //键值冲突异常,用下方方法替换,由新值覆盖旧值
            mapDto = loanList.stream().collect(Collectors.toMap(LoanDto::getUserId,
                    loanDto->LoanDto.builder()
                            .loanAmt(loanDto.getLoanAmt())
                            .planNum(loanDto.getPlanNum())
                            .loanId(loanDto.getLoanId()).build(),
                    (oldValue,newValue)->newValue
            ));
        }
        logger.info("mapDto:{}", mapDto);

        //默认value加法操作,相同的key加法汇总运算
        Map<String,BigDecimal> mapAdd1 = loanList.stream().collect(Collectors.toConcurrentMap(
                LoanDto::getUserId,
                LoanDto::getLoanAmt,
                BigDecimal::add));
        logger.info("mapAdd1:{}", mapAdd1);

        //手动value合并操作,key相同时,返回value中的大者
        Map<String,BigDecimal> mapMax1 = loanList.stream().collect(Collectors.toConcurrentMap(
                LoanDto::getUserId,
                LoanDto::getLoanAmt,
                (loanAmt1,loanAmt2)->loanAmt1.compareTo(loanAmt2)>0 ? loanAmt1 : loanAmt2));
        logger.info("mapMax1:{}", mapMax1);

        //手动value合并操作,key相同时,返回value中的大者
        //第3个参数用于解决,key冲突时处理
        //第4个参数用于指定生成的Map类型
        ConcurrentMap<String,BigDecimal> cMapMax2 = loanList.stream().collect(Collectors.toConcurrentMap(
                LoanDto::getUserId,
                LoanDto::getLoanAmt,
                (loanAmt1,loanAmt2)->loanAmt1.compareTo(loanAmt2)<0 ? loanAmt1 : loanAmt2,
                ConcurrentSkipListMap::new));
        logger.info("cMapMax2:{}", cMapMax2);

        //第2个方法,将第一个方法的结果作为参数
        //先对planNum取平均数,再转换为int
        int avgInt2 = loanList.stream().collect(Collectors.collectingAndThen(Collectors.averagingInt(LoanDto::getPlanNum), Double::intValue));
        logger.info("avgInt2:{}", avgInt2);

        //先找到分期号最小的贷款单,然后通过get返回
        LoanDto loanTmp = loanList.stream().collect(Collectors.collectingAndThen(
                Collectors.minBy(Comparator.comparingInt(LoanDto::getPlanNum)),
                Optional::get));
        logger.info("loanTmp:{}", loanTmp);

    }

}
