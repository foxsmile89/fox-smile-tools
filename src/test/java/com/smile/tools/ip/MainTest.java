package com.smile.tools.ip;

/**
 * @className MainTest
 * @description Ip工具类main测试
 * @author zhaojinyong
 * @date 2018-06-01 10:50:00
 * @version 1.0
 **/
public class MainTest {

    public static void main(final String[] args) {
        final String ipStr = "192.185.11.11";
        System.out.println(IpNumInterConvert.getIpNum(ipStr));

        final Long ipNum = 3233352459L;
        System.out.println(IpNumInterConvert.getIpString(ipNum));
    }

}
