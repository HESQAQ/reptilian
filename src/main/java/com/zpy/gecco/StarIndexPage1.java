package com.zpy.gecco;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.request.HttpGetRequest;

import java.util.Date;

/**
 * Created by Administrator on 2018/7/31.
 */
public class StarIndexPage1 {
    public static void main(String[] args) {
        //String url = "http://ent.sina.com.cn/ku/star_search_index.d.html?page=1&cTime=1532747982&pre=next"; //想要爬取的网站的首页地址
        String url = "http://ent.sina.com.cn/ku/star_search_index.d.html"; //想要爬取的网站的首页地址
        HttpGetRequest start = new HttpGetRequest(url); //获取网站请求
        start.setCharset("UTF-8");
        GeccoEngine.create() //创建搜索引擎
                .classpath("com.zpy.gecco") //要搜索的包名，会自动搜索该包下，含@Gecco注解的文件。
                .start(start)
                .thread(1)//开启多少个线程抓取
                .interval(2000) //隔多长时间抓取1次
                .run();
    }

}


