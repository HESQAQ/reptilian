package com.zpy.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Administrator on 2018/8/1.
 */
public class demo {
    
    public static void main(String[] args) throws IOException {
        String a="<div class=\"pkg_page basefix\">\n" +
                "    <a href=\"/dest/u-_cf_e5_d1_f4/s-tickets/P2/\" class=\"up\">\n" +
                "        <b></b></a>\n" +
                "    \n" +
                "    <a href=\"/dest/u-_cf_e5_d1_f4/s-tickets/P1/\" class=\"\">\n" +
                "        1</a>\n" +
                "    \n" +
                "    <a href=\"/dest/u-_cf_e5_d1_f4/s-tickets/P2/\" class=\"\">\n" +
                "        2</a>\n" +
                "    \n" +
                "    <a href=\"###\" class=\"current\">\n" +
                "        3</a>\n" +
                "    \n" +
                "    <a href=\"###\" class=\"down down_nocurrent\">\n" +
                "        下一页<b></b></a> <span class=\"pkg_pagevalue\">到<input id=\"iptPageTxt\" type=\"text\" class=\"pkg_page_num\" data-name=\"CurrentPages\">页<input id=\"iptPageBtn\" type=\"button\" class=\"pkg_page_submit\" value=\"确定\"></span>\n" +
                "</div>";

       // Document ttt=Jsoup.parse(a);
        Document ttt=Jsoup.connect("http://piao.ctrip.com/dest/u-_ba_bc_d6_dd/s-tickets/P2/").get();

        Elements d=ttt.select("div.pkg_page.basefix > a");
        System.out.println("-----"+d.get(d.size()-2).html());
        Element fff=ttt.selectFirst("div.pkg_page.basefix > a.current");
        System.out.println("fff:"+fff.html());
        Elements w=ttt.select("div.pkg_page.basefix > a");
        System.out.println("==="+w.prev("a"));
        System.out.println("==="+d.last());
        Element xx=d.last();
        Elements s=d.eq(4);
        System.out.println("==="+d);
        System.out.println("==="+s);
       /* Document doc = Jsoup.connect("https://github.com/xtuhcy/gecco/").get();
        Elements d=doc.getElementsByClass("repository-meta-content");
        Elements links = doc.select("a[href]"); // a with href
        Elements pngs = doc.select("body > div > h1");
        System.out.println("==="+links);
        System.out.println("===="+links.attr("href"));
        System.out.println("===="+pngs.toString());*/

    }

}
