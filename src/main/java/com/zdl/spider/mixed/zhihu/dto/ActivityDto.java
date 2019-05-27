package com.zdl.spider.mixed.zhihu.dto;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户动态
 * <p>
 * Created by ZDLegend on 2019/4/28 15:10
 */
public class ActivityDto {

    private String id;
    private String actionText;
    private AuthorDto actor;
    private long createdTime;
    private JSONObject target;
    private String type;
    private String verb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public AuthorDto getActor() {
        return actor;
    }

    public void setActor(AuthorDto actor) {
        this.actor = actor;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public JSONObject getTarget() {
        return target;
    }

    public void setTarget(JSONObject target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }
}
