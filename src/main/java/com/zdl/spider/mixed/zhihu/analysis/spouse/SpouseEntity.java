package com.zdl.spider.mixed.zhihu.analysis.spouse;

import java.util.Date;

/**
 * 择偶标准实体类
 *
 * Created by ZDLegend on 2019/6/5 10:58
 */
public class SpouseEntity {

    //key questionId + "_" + answerId
    private String id;

    //作者id
    private String authorId;

    //性别 0女 1男 2未知
    private int gender;

    //出生
    private Date born;

    //升高
    private Integer high;

    //体重
    private Integer weight;

    //回答内容
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
