package com.smile.tools.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ip工具类main测试
 * @author zhaojinyong
 * @date 2018-06-01 10:50:00
 * @version 1.0
 **/
public class MainExcute {

    private static final Logger logger = LoggerFactory.getLogger(MainExcute.class);

    public static void main(final String[] args) {
        final String ipStr = "192.185.11.11";
        logger.info("ipStr:{},ipNum:{}", ipStr, IpNumInterConvert.getIpNum(ipStr));

        final Long ipNum = 3233352459L;
        logger.info("ipNum:{},ipStr:{}", ipNum, IpNumInterConvert.getIpString(ipNum));
    }

}
