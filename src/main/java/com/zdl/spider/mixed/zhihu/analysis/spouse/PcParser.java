package com.zdl.spider.mixed.zhihu.analysis.spouse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ZDLegend on 2019/6/27 17:57
 */
public class PcParser {

    private static Map<String, List<String>> pcMap = new HashMap<>();

    static {
        getFile();
        pcMap.put("香港", new ArrayList<>());
        pcMap.put("澳门", new ArrayList<>());
        pcMap.put("台湾", new ArrayList<>());
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
                    .map(s1 -> s1.replace("市","").replace("区",""))
                    .collect(Collectors.toList());
            String ss = key.replace("省", "")
                    .replace("市","")
                    .replace("自治区","");

            if(ss.contains("新疆")) {
                ss = "新疆";
            } else if(ss.contains("西藏")) {
                ss = "西藏";
            } else if(ss.contains("广西")) {
                ss = "广西";
            } else if(ss.contains("内蒙古")) {
                ss = "内蒙古";
            } else if(ss.contains("宁夏")) {
                ss = "宁夏";
            }

            pcMap.put(ss, list);
        });
    }

    public static Map<String, List<String>> getPcMap() {
        return pcMap;
    }
}
