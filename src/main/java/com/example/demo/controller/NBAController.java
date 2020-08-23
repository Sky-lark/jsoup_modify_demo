package com.example.demo.controller;

import com.example.demo.services.NBAService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@RestController
@RequestMapping("/nba")
public class NBAController {

    @Autowired
    NBAService service;

    @RequestMapping("index")
    public String Index() {
        Document document = null;
        try {
            document = Jsoup.connect("http://jrkan.com").get();
            service.filterNav(document);
            Elements aTag = document.select("#loc-data-list > div.loc_match_list > div:nth-child(4) > div > ul > li.lab_channel > a");
            // 修改直播的连接，使其链接到自己的网站
            aTag.forEach(element -> {
                String href = element.attr("href");
                String play = element.attr("data-play");
                try {
                    href = "play/" + Base64.getEncoder().encodeToString((href).getBytes("utf-8"));
                    href = "play/" + Base64.getEncoder().encodeToString((play).getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                element.attr("href", href);
                element.attr("data-play", play);
                ;
            });

        } catch (IOException e) {
            e.printStackTrace();
            return "错误";
        }

        return document.outerHtml();
    }

    @RequestMapping("play/{url}")
    public String play(@PathVariable("url") String url) {
        Document document = null;
        try {
            url = new String(Base64.getDecoder().decode(url), "utf-8");
            document = Jsoup.connect(url).get();
            service.filterNav(document);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return document.outerHtml();
    }


}
