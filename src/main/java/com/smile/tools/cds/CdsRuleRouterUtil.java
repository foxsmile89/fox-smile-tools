package com.smile.tools.cds;


import lombok.extern.slf4j.Slf4j;

/**
 * 库表hash规则工具类
 * @author smile
 * @date 2019-07-10 10:50:00
 * @version 1.0
 **/
@Slf4j
public class CdsRuleRouterUtil {


    private CdsRuleRouterUtil(){}

    /**
     * 分库数量
     */
    public static final int DB_COUNT = 24;

    /**
     * 单库分表数量
     */
    public static final int TABLE_COUNT = 50;


    /**
     * 计算库名后缀
     * @param splitKeyValue 切分键值
     * @return 2位库名后缀
     */
    public static String routeDB(String splitKeyValue) {
        //(Math.abs(splitKeyValue.hashCode()) % 1200) / 50
        int dbSuffix = Math.abs(splitKeyValue.hashCode()) % (DB_COUNT*TABLE_COUNT) / TABLE_COUNT;
        return String.format("%02d", dbSuffix);
    }

    /**
     * 计算表名后缀
     * @param splitKeyValue 切分键值
     * @return 4位表名后缀
     */
    public static String routeTable(String splitKeyValue) {
        //(Math.abs(splitKeyValue.hashCode()) % 50)
        int tableSuffix = Math.abs(splitKeyValue.hashCode()) % (TABLE_COUNT) ;
        return String.format("%04d", tableSuffix);
    }

    public static void main(String[] args){
        String key = "161202220715619440";
        String db = routeDB(key);
        String tab = routeTable(key);
        log.info("db:{},tab:{}", db, tab);
    }

}