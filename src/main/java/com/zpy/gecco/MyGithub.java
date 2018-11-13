package com.zpy.gecco;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.*;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.spider.HtmlBean;
import lombok.Data;

/**
 * Created by Administrator on 2018/8/1.
 */
@Data
@Gecco(matchUrl="https://github.com/{user}/{project}", pipelines={"consolePipeline", "aaaa"})
public class MyGithub implements HtmlBean {

    private static final long serialVersionUID = -7127412585200687225L;

    @RequestParameter("user")
    private String user;

    @RequestParameter("project")
    private String project;

    @Text
    @HtmlField(cssPath=".repository-meta-content")
    private String title;

    @Text
    @HtmlField(cssPath=".pagehead-actions li:nth-child(2) .social-count js-social-count")
    private String star;

    @Text
    @HtmlField(cssPath=".pagehead-actions li:nth-child(3) .social-count")
    private String  fork;

    @Html
    @HtmlField(cssPath=".entry-content")
    private String readme;


    public static void main(String[] args) {
        HttpGetRequest start = new HttpGetRequest("https://github.com/xtuhcy/gecco"); //获取网站请求
        start.setCharset("UTF-8");
        GeccoEngine.create()
                .classpath("com.zpy.gecco")
                .start(start)
                .thread(1)
                .interval(2000)
                .loop(true)
                .mobile(false)
                .start();
    }
}
