package com.zdl.spider.mixed.railway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Otn12306 extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(Otn12306.class);

    private String date;

    private String from = "HZH";

    private String to = "TSJ";

    private String rail = "G1874";

    private HttpClient httpClient = HttpClient.newHttpClient();

    private int num = 0;

    private volatile boolean running = true;

    public Otn12306(String date){
        this.date = date;
    }

    public Otn12306(String date, String rail, String from, String to){
        this.to = to;
        this.from = from;
        this.rail = rail;
        this.date = date;
    }

    public static void main(String[] args) {
        //new Otn12306("2019-02-01", "G1874", "HZH", "TSJ").start();
        //new Otn12306("2019-02-02", "G1874", "HZH", "TSJ").start();
        //new Otn12306("2019-02-03", "G1874", "HZH", "TSJ").start();

        new Otn12306("2019-02-10", "G1876", "TSJ", "HGH").start();
        new Otn12306("2019-02-11", "G1876", "TSJ", "HGH").start();
    }

    public void http(HttpClient httpClient) throws ExecutionException, InterruptedException {

        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date="
                        + date + "&leftTicketDTO.from_station=" + from + "&leftTicketDTO.to_station=" + to + "&purpose_codes=ADULT"))
                .header("X-Requested-With", "XMLHttpRequest")
                .header("If-Modified-Since", "0")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                .timeout(Duration.ofMillis(5009))
                .build();

        CompletableFuture<Void> future = httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::analysis);

        future.get();
    }

    public void analysis(String s){

        if(s.contains("403 Forbidden")){
            logger.error("403 Forbidden");
            return;
        }

        var json = JSON.parseObject(s);
        if(json == null){
            logger.error(s);
            return;
        }

        var data = json.getJSONObject("data");
        if(data != null){
            JSONArray array = data.getJSONArray("result");
            array.stream().filter(o -> String.valueOf(o).contains(rail)).findFirst().ifPresent(o1 -> {
                String[] ss = String.valueOf(o1).split("\\|");
                List<String> list = Arrays.asList(ss);
                logger.info("{}查询第{}次：{}", date, num, check(list));
            });
        }
    }

    public String check(List<String> list){

        var secondClass = getTicketNum(list.get(30));
        var firstClass = getTicketNum(list.get(31));
        var commerce = getTicketNum(list.get(32));

        var result = "二等座（" + secondClass + "), 一等座（" + firstClass + "), 商务（" + commerce + ")";

        if(secondClass > 0){
            jump(result);
        }

        return result;
    }

    private int getTicketNum(String t){
        if("有".equals(t)) {
            return 999;
        }

        try {
            return Integer.valueOf(t);
        } catch (Exception e){
            return 0;
        }
    }

    public void jump(String result){

        running = false;

        JFrame frame = new JFrame("发现剩余火车票:" + new Date());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JButton b = new JButton("继续");
        b.addActionListener(e -> new Otn12306(date).start());
        frame.add(b);

        JLabel label = new JLabel(date + " 发现剩余火车票: " + result);
        frame.getContentPane().add(label);
    }

    @Override
    public void run() {
        while (running){
            try {
                http(httpClient);
                num++;
                Thread.sleep(1000);
            } catch (ExecutionException e) {
                logger.error(e.getMessage(), e);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
