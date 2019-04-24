package com.zdl.spider.mixed.zhihu;

/**
 * Created by ZDLegend on 2019/4/24 10:29
 */
public class ZhihuSpiderException extends RuntimeException {
    public ZhihuSpiderException(String msg) {
        super(msg);
    }

    public ZhihuSpiderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
