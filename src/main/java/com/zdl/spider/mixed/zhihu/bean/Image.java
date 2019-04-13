package com.zdl.spider.mixed.zhihu.bean;

import com.zdl.spider.mixed.utils.HttpUtil;
import com.zdl.spider.mixed.zhihu.entity.AnswerEntity;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * 图片对象
 * <p>
 * Created by ZDLegend on 2019/4/12 16:01
 */
public class Image {

    /**
     * 图片路径
     */
    private String path;

    /**
     * 图片所在回答
     */
    private AnswerEntity answerEntity;

    private Image() {
    }

    public static Image of(String path, AnswerEntity answerEntity) {
        Image image = new Image();
        image.answerEntity = answerEntity;
        image.path = path;
        return image;
    }

    /**
     * 直接保存图片
     *
     * @param localPath 本地路径
     */
    public CompletableFuture<Void> directSave(String localPath) {
        return HttpUtil.downLoadFile(path, localPath);
    }

    /**
     * 按照作者+问题的路径保存图片
     *
     * @param basePath 本地基础路径
     */
    public CompletableFuture<Void> saveByAuthorThenQuestion(String basePath) {
        String absolutePath = basePath + File.separator + answerEntity.getAuthor().getName() + File.separator + answerEntity.getQuestion().getTitle();
        return HttpUtil.downLoadFile(path, absolutePath);
    }

    /**
     * 按照问题+作者的路径保存图片
     *
     * @param basePath 本地基础路径
     */
    public CompletableFuture<Void> saveByQuestionThenAuthor(String basePath) {
        String absolutePath = basePath + File.separator + answerEntity.getQuestion().getTitle() + File.separator + answerEntity.getAuthor().getName();
        return HttpUtil.downLoadFile(path, absolutePath);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AnswerEntity getAnswerEntity() {
        return answerEntity;
    }

    public void setAnswerEntity(AnswerEntity answerEntity) {
        this.answerEntity = answerEntity;
    }
}
