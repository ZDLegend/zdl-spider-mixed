package com.zdl.spider.mixed.zhihu.resources;

import com.zdl.spider.mixed.utils.FileUtil;
import com.zdl.spider.mixed.utils.HttpUtil;
import com.zdl.spider.mixed.zhihu.dto.AnswerDto;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * 图片对象
 * <p>
 * Created by ZDLegend on 2019/4/12 16:01
 */
public class Image implements Resource {

    /**
     * 图片路径
     */
    private String path;

    /**
     * 图片文件名
     */
    private String name;

    /**
     * 图片所在回答
     */
    private AnswerDto answerDto;

    private Image() {
    }

    public static Image of(String path, AnswerDto answerDto) {
        Image image = new Image();
        image.answerDto = answerDto;
        image.path = path;
        image.name = path.substring(path.lastIndexOf('/') + 1);
        return image;
    }

    /**
     * 直接保存图片
     *
     * @param localPath 本地路径
     */
    public CompletableFuture<Void> directSave(String localPath) {
        return HttpUtil.downLoadFile(path, localPath, name);
    }

    /**
     * 按照作者+问题的路径保存图片
     *
     * @param basePath 本地基础路径
     */
    public CompletableFuture<Void> saveByAuthorThenQuestion(String basePath) {
        String absolutePath = basePath
                + File.separator + FileUtil.removeIllegalWord(answerDto.getAuthor().getName())
                + File.separator + FileUtil.removeIllegalWord(answerDto.getQuestion().getTitle());
        return HttpUtil.downLoadFile(path, absolutePath, name);
    }

    /**
     * 按照问题+作者的路径保存图片
     *
     * @param basePath 本地基础路径
     */
    public CompletableFuture<Void> saveByQuestionThenAuthor(String basePath) {
        String absolutePath = basePath
                + File.separator + FileUtil.removeIllegalWord(answerDto.getQuestion().getTitle())
                + File.separator + FileUtil.removeIllegalWord(answerDto.getAuthor().getName());
        return HttpUtil.downLoadFile(path, absolutePath, name);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AnswerDto getAnswerDto() {
        return answerDto;
    }

    public void setAnswerDto(AnswerDto answerDto) {
        this.answerDto = answerDto;
    }
}
