package com.zdl.spider.mixed.zhihu.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ZDLegend on 2019/5/30 17:17
 */
@Entity
@Table(name = "zhihu_answer")
public class AnswerEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "answer_type")
    private String answerType;

    @Column(name = "question_id")
    private String questionId;

    @Column(name = "author_id")
    private String authorId;

    @Column(name = "name")
    private String name;

    @Column(name = "created_time")
    private long createdTime;

    @Column(name = "updated_time")
    private long updatedTime;

    @Column(name = "content")
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
