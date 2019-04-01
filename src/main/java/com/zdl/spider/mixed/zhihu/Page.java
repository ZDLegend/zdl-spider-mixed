package com.zdl.spider.mixed.zhihu;

/**
 * Created by ZDLegend on 2019/4/1 9:29
 */
public class Page {

    private boolean isEnd;
    private String next;

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
