package com.smile.tools.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ip和数字互转
 * @author smile
 * @date 2018-06-01 10:50:00
 * @version 1.0
 **/
public class IpNumInterConvert {

    private static final Logger logger = LoggerFactory.getLogger(IpNumInterConvert.class);

    /**
     * IP分段数
     */
    private static final Integer IP_SEG_NUM = 4;

    private IpNumInterConvert(){}

    /**
     * 将IP字符串转成数字
     * @param ip IP字符串
     * @return IP数字
     */
    public static Long getIpNum(final String ip) {
        Long ipNum = 0L;
        if(null == ip){
            return ipNum;
        }

        final String ipStr = ip.trim();
        if (ipStr.length() != 0) {
            final String[] subips = ipStr.split("\\.");
            for (final String str : subips) {
                // 向左移8位
                ipNum = ipNum << 8;
                ipNum += Integer.parseInt(str);
                logger.info("ipNum:{}", ipNum);
            }
        }
        return ipNum;
    }

    /**
     * 将IP字符串转成数字
     * @param ipNum IP数字
     * @return IP字符串
     */
    public static String getIpString(final Long ipNum) {
        final Long[] andNumbers = { 0xff000000L, 0x00ff0000L, 0x0000ff00L, 0x000000ffL };
        final StringBuilder ipStrSb = new StringBuilder();
        for (int i = 0; i < IP_SEG_NUM; i++) {
            ipStrSb.append(String.valueOf((ipNum & andNumbers[i]) >> 8 * (3 - i)));
            if (i != 3) {
                ipStrSb.append(".");
            }
        }
        return ipStrSb.toString();
    }

}

