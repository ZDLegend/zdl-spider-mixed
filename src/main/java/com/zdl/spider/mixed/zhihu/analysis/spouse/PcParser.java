package com.zdl.spider.mixed.zhihu.analysis.spouse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.FileUtil;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ZDLegend on 2019/6/27 17:57
 */
public class PcParser {

    /**
     * 全国省市map
     */
    private static Map<String, List<String>> pcMap = new HashMap<>();

    /**
     * 直辖市及香港澳门列表
     */
    private static List<String> specialCities = new ArrayList<>();

    static {
        getFile();
        pcMap.put("香港", new ArrayList<>());
        pcMap.put("澳门", new ArrayList<>());
        pcMap.put("台湾", new ArrayList<>());

        specialCities.add("北京");
        specialCities.add("天津");
        specialCities.add("上海");
        specialCities.add("重庆");
        specialCities.add("香港");
        specialCities.add("澳门");
    }

    public static void main(String[] args) {
        System.out.println(pcMap);
    }

    private static void getFile() {
        String s = FileUtil.readClassPathFileToString("pc.json");
        JSONObject json = JSON.parseObject(s);
        json.keySet().forEach(key -> {
            List<String> list = json.getJSONArray(key)
                    .stream()
                    .map(String::valueOf)
                    .map(s1 -> s1.replace("市", "").replace("区", ""))
                    .collect(Collectors.toList());
            String ss = key.replace("省", "")
                    .replace("市", "")
                    .replace("自治区", "");

            if (ss.contains("新疆")) {
                ss = "新疆";
            } else if (ss.contains("西藏")) {
                ss = "西藏";
            } else if (ss.contains("广西")) {
                ss = "广西";
            } else if (ss.contains("内蒙古")) {
                ss = "内蒙古";
            } else if (ss.contains("宁夏")) {
                ss = "宁夏";
            }

            pcMap.put(ss, list);
        });
    }

    public static MutablePair<String, String> getEarth(String sem) {
        MutablePair<String, String> pair = new MutablePair<>();
        PcParser.getPcMap().forEach((k, v) -> {
            if (sem.contains(k)) {
                pair.setLeft(k);
            } else if (sem.contains("魔都")) {
                pair.setLeft("上海");
            } else if (sem.contains("帝都")) {
                pair.setLeft("北京");
            }

            v.forEach(s -> {
                if (sem.contains(s)) {
                    pair.setRight(s);
                }
            });

            if (PcParser.getSpecialCities().contains(pair.getLeft())) {
                pair.setRight(pair.getLeft());
            }
        });
        return pair;
    }

    public static Map<String, List<String>> getPcMap() {
        return pcMap;
    }

    public static List<String> getSpecialCities() {
        return specialCities;
    }
}
