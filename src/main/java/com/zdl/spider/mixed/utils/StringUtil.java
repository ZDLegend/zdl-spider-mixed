package com.zdl.spider.mixed.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ZDLegend on 2019/6/18 10:48
 */
public class StringUtil {

    // 判断一个字符串是否都为数字
    public boolean isDigit(String strNum) {
        return strNum.matches("[0-9]+");
    }

    //提取字符串中所有数字
    public List<Integer> getDigit(String content) {
        String[] cs = Pattern.compile("[^0-9]+").split(content);
        return Stream.of(cs).distinct()
                .filter(StringUtils::isNotBlank)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
