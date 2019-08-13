package com.zdl.spider.mixed.zhihu.analysis.spouse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 择偶标准实体类
 * <p>
 * Created by ZDLegend on 2019/6/5 10:58
 */
@Entity
@Table(name = "zhihu_spouse")
public class SpouseEntity {

    //key questionId + "_" + answerId
    @Id
    @Column(name = "id")
    private String id;

    //作者id
    @Column(name = "author_id")
    private String authorId;

    //性别 0女 1男 2未知
    @Column(name = "gender")
    private Integer gender;

    //年龄
    @Column(name = "age")
    private Integer age;

    //身高
    @Column(name = "high")
    private Integer high;

    //体重
    @Column(name = "weight")
    private Integer weight;

    //所在地区
    @Column(name = "province")
    private String province;

    //城市
    @Column(name = "city")
    private String city;

    //学历
    @Column(name = "education")
    private String education;

    @Column(name = "is_211")
    private Boolean is211 = null;

    @Column(name = "is_985")
    private Boolean is985 = null;

    //回答内容
    @Column(name = "content")
    private String content;

    //有无照片
    @Column(name = "has_pic")
    private Boolean hasPic;

    //自身条件
    @Column(name = "self")
    private String self;

    //要求
    @Column(name = "demand")
    private String demand;

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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
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

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Boolean getIs211() {
        return is211;
    }

    public void setIs211(Boolean is211) {
        this.is211 = is211;
    }

    public Boolean getIs985() {
        return is985;
    }

    public void setIs985(Boolean is985) {
        this.is985 = is985;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getHasPic() {
        return hasPic;
    }

    public void setHasPic(Boolean hasPic) {
        this.hasPic = hasPic;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }
}
