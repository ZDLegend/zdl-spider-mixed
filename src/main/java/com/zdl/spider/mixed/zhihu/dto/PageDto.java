package com.zdl.spider.mixed.zhihu.dto;

/**
 * Created by ZDLegend on 2019/4/1 9:29
 */
public class PageDto {

    private boolean isEnd;
    private boolean isStart;
    private String next;
    private String previous;
    private int totals;

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

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }
}
