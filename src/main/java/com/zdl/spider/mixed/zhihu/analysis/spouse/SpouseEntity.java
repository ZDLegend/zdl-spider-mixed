package com.zdl.spider.mixed.zhihu.analysis.spouse;

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

    //年龄
    private Integer age;

    //身高
    private Integer high;

    //体重
    private Integer weight;

    //所在地区
    private String province;

    //城市
    private String city;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
