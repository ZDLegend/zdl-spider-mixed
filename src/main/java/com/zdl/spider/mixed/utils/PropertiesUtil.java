package com.zdl.spider.mixed.utils;

import java.util.ResourceBundle;

/**
 * 手动获取资源配置
 * <p>
 * Created by ZDLegend on 2019/8/21 17:46
 */
public class PropertiesUtil {

    public static final ResourceBundle RESOURCE = ResourceBundle.getBundle("application");

    public static final String HTTP_POXY_ADDR = "http.poxy.addr";
    public static final String HTTP_POXY_PORT = "http.poxy.port";

    /**
     * 是否配置HTTP代理
     */
    public static boolean isHttpPoxy() {
        return RESOURCE.containsKey(HTTP_POXY_ADDR)
                && RESOURCE.containsKey(HTTP_POXY_PORT);
    }
}
