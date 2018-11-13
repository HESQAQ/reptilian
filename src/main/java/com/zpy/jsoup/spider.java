package com.zpy.jsoup;


import java.io.File;
import java.io.IOException;


import com.zpy.spider.HttpClient;
import com.zpy.spider.JavaHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Administrator on 2018/7/31.
 */
public class spider {
    public static void main(String[] args) throws IOException {
        String url = "http://www.baidu.com";
        Document document = Jsoup.connect(url).timeout(3000).get();//链接网页返回网页的dom对象
       // System.out.println("====document"+document);


        //通过Document的select方法获取属性结点集合
        Elements elements = document.select("a[href]");
        //得到节点的第一个对象
        Element element = elements.get(0);
        //System.out.println(element);



        //使用http的get方式获取返回的网页信息
        HttpClient h=new JavaHttpClient();
        String s = h.doGet("http://www.baidu.com",null).getResponseString();
        //System.out.println("======"+s);
        Document root_document = Jsoup.parse(s);//将html的字符串解析成一个html文档
        //获取所有a标签元素
        Elements e=root_document.getElementsByTag("a");
        //循环获取a标签的href属性
        for(int i=0;i<e.size();i++){
            Element href=e.get(i);//得到每一个a标签
            String link=href.attr("href");//获取href属性
            System.out.println("====获取到的链接："+link);
        }


    }

}
