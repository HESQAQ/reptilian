package com.zpy.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/20.
 */
public class TestMain {
    public static void main(String[] args) throws IOException {
        String url="http://hotels.ctrip.com/Domestic/tool/AjaxHotelCommentList.aspx?MasterHotelID=428365&hotel=428365&NewOpenCount=0&AutoExpiredCount=0&RecordCount=6508&OpenDate=&card=-1&property=-1&userType=-1&productcode=&keyword=&roomName=&orderBy=2&currentPage=2&viewVersion=c&contyped=0&eleven=f2d7052df31501843434a23c2edb53ca1721a4ed85bd7e14639657e923e1662c&callback=CASpIhdpQFdTbceQO&_=1537422469974";
        String u="http://hotels.ctrip.com/hotel/428365.html?isFull=F#ctm_ref=hod_sr_lst_dl_n_1_1";
        HttpClient h=new JavaHttpClient();
        Map headers=new HashMap();
        headers.put("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");

        headers.put("Referer","http://hotels.ctrip.com/hotel/428365.html?");
        String s = h.doGet("http://you.ctrip.com/sight/linan88/51377.html",null).getResponseString();
        System.out.println("===="+s);
        Document root_document = Jsoup.parse(s);
        Element sco=root_document.selectFirst("ul.detailtop_r_info > li > span.score > b");
        String score=sco.text();
        int rate=(int)Math.ceil((Double.parseDouble(score)/5)*100);
        System.out.println("==="+rate);
        Element div=root_document.selectFirst("div.detailcon > div.toggle_l > div.text_style > p:nth-child(1)");
        String intro=div==null?null:div.text();
        Element tatol=root_document.selectFirst("#weiboComment > ul > li.active > h2 > span > span");
        int ta=tatol==null?null:Integer.parseInt(tatol.text());
        Element s1=root_document.selectFirst("#weiboCom1 > div.comment_count.cf > dl.comment_show > dd:nth-child(2) > span.score");
        double score1=s1==null?null:Double.parseDouble(s1.text());
        Element s2=root_document.selectFirst("#weiboCom1 > div.comment_count.cf > dl.comment_show > dd:nth-child(3) > span.score");
        double score2=s2==null?null:Double.parseDouble(s2.text());
        Element s3=root_document.selectFirst("#weiboCom1 > div.comment_count.cf > dl.comment_show > dd:nth-child(4) > span.score");
        double score3=s3==null?null:Double.parseDouble(s3.text());
        Element s4=root_document.selectFirst("#wwwww > div.comment_count.cf > dl.comment_show > dd:nth-child(4) > span.score");
    }
}
