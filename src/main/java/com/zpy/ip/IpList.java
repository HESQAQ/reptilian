package com.zpy.ip;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.annotation.RequestParameter;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;
import lombok.Data;

@Data
@Gecco(matchUrl="https://www.kuaidaili.com/free/inha/{currPage}/", pipelines={"consolePipeline", "IpListPipeline"})
public class IpList implements HtmlBean {

    private static final long serialVersionUID = 4544492736813943899L;

    @Request
    private HttpRequest request;

    @RequestParameter("currPage")
    private String currPage;

    /**
     * 抓取列表项的详细内容，包括ip，端口等
     */


    @HtmlField(cssPath="#list > table")
    private List<IpDetail> ips;


    @HtmlField(cssPath = "#list > table > tbody")
    private String iop;







    public static void main(String[] args) {
        HttpGetRequest start = new HttpGetRequest("https://www.kuaidaili.com/free/inha/1/"); //获取网站请求
        GeccoEngine.create()
                .classpath("com.zpy.ip")
                //开始抓取的页面地址
                .start(start)
                //开启几个爬虫线程,线程数量最好不要大于start request数量
                .thread(1)
                //单个爬虫每次抓取完一个请求后的间隔时间
                .interval(5000)
                .run();
    }
}
