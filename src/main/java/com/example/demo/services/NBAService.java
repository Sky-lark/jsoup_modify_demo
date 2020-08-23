package com.example.demo.services;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class NBAService {

    public void filterNav(Document document) {

        Elements select = document.select("body > div.web_phone > div.web_nav_menu");
        select.html("");
        document.title("快乐小站");
        Elements copyRight = document.select("#loc-copyright");
        copyRight.html("");

    }
}
