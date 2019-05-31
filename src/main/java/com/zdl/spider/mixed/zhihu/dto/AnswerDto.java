package com.zdl.spider.mixed.zhihu.dto;

import com.zdl.spider.mixed.zhihu.web.entity.AnswerEntity;
import org.springframework.beans.BeanUtils;

/**
 * 知乎回答对象
 * <p>
 * Created by ZDLegend on 2019/4/2 17:25
 */
public class AnswerDto {

    private String id;
    private String type;
    private String answerType;
    private QuestionDto question;
    private AuthorDto author;
    private String url;
    private String name;
    private long createdTime;
    private long updatedTime;

    //点赞数
    private int voteupCount;

    //评论数
    private int commentCount;

    private String content;

    public static AnswerEntity toEntity(AnswerDto dto) {
        AnswerEntity answerEntity = new AnswerEntity();
        BeanUtils.copyProperties(dto, answerEntity);

        if(dto.author != null) {
            answerEntity.setAuthorId(dto.author.getId());
        }

        if(dto.question != null) {
            answerEntity.setAuthorId(dto.question.getId());
        }
        
        return answerEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public QuestionDto getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDto question) {
        this.question = question;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getVoteupCount() {
        return voteupCount;
    }

    public void setVoteupCount(int voteupCount) {
        this.voteupCount = voteupCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
