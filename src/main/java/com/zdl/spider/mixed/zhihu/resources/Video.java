package com.zdl.spider.mixed.zhihu.resources;

import com.zdl.spider.mixed.utils.FileUtil;
import com.zdl.spider.mixed.zhihu.dto.AnswerDto;
import com.zdl.spider.mixed.zhihu.strategy.VideoStrategy;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import static com.zdl.spider.mixed.zhihu.strategy.VideoStrategy.VIDEO_DRESS;

/**
 * 视频对象
 * <p>
 * Created by ZDLegend on 2019/4/23 16:02
 */
public class Video implements Resource {

    /**
     * 视频路径
     */
    private String path;

    /**
     * 视频文件名
     */
    private String name;

    /**
     * 视频所在回答
     */
    private AnswerDto answerDto;

    private Video() {
    }

    public static Video of(String path, AnswerDto answerDto) {
        Video video = new Video();
        video.answerDto = answerDto;
        video.path = path;
        video.name = path.substring(path.lastIndexOf('/') + 1) + ".mp4";
        return video;
    }

    public static Video ofId(String id, AnswerDto answerDto) {
        Video video = new Video();
        String path = String.format(VIDEO_DRESS, id);
        video.answerDto = answerDto;
        video.path = path;
        video.name = id + ".mp4";
        return video;
    }

    /**
     * 直接保存视频
     *
     * @param localPath 本地路径
     */
    public CompletableFuture<Void> directSave(String localPath) {
        return VideoStrategy.videoDownload(path, localPath, name);
    }

    /**
     * 按照作者+问题的路径保存视频
     *
     * @param basePath 本地基础路径
     */
    public CompletableFuture<Void> saveByAuthorThenQuestion(String basePath) {
        String absolutePath = basePath
                + File.separator + FileUtil.removeIllegalWord(answerDto.getAuthor().getName())
                + File.separator + FileUtil.removeIllegalWord(answerDto.getQuestion().getTitle());
        return VideoStrategy.videoDownload(path, absolutePath, name);
    }

    /**
     * 按照问题+作者的路径保存视频
     *
     * @param basePath 本地基础路径
     */
    public CompletableFuture<Void> saveByQuestionThenAuthor(String basePath) {
        String absolutePath = basePath
                + File.separator + FileUtil.removeIllegalWord(answerDto.getQuestion().getTitle())
                + File.separator + FileUtil.removeIllegalWord(answerDto.getAuthor().getName());
        return VideoStrategy.videoDownload(path, absolutePath, name);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnswerDto getAnswerDto() {
        return answerDto;
    }

    public void setAnswerDto(AnswerDto answerDto) {
        this.answerDto = answerDto;
    }
}
