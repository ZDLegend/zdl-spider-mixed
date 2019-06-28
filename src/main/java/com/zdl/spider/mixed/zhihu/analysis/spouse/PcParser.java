package com.zdl.spider.mixed.zhihu.analysis.spouse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdl.spider.mixed.utils.FileUtil;

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
            String ss = key.replace("省", "").replace("市","");
            pcMap.put(ss, list);
        });
    }

    public static Map<String, List<String>> getPcMap() {
        return pcMap;
    }
}
