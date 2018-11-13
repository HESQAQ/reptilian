package com.zpy.spider;

import com.zpy.QButil.DBQuerier;
import com.zpy.dao.dataobject.City;
import com.zpy.dao.dataobject.Color;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**爬取携程酒店城市信息
 * Created by Administrator on 2018/8/1.
 */
public class SpiderCity {
    private static BlockingQueue<Object[]> insertqueue = new LinkedBlockingQueue<>();//城市
    private static BlockingQueue<Object[]> insertqueue2 = new LinkedBlockingQueue<>();//景点

    public static void main(String[] args) throws IOException {


        //getJd();
        getCity();


    }


    public void  insert(String sql,BlockingQueue<Object[]>  queue){
        new Thread(){
            @Override
            public void run() {
                while(true){
                    if(queue.size()>0){
                        QueryRunner runner = DBQuerier.getInstance().getQueryRunner();
                        int tempcount = 500;
                        if (tempcount > queue.size()) {
                            tempcount = queue.size();
                        }
                        Object tempobj[][] = new Object[tempcount][];
                        for (int i =0; i < tempcount; i++) {
                            Object obj[] = queue.poll();
                            if (null != obj) {
                                tempobj[i] = obj;
                            }
                        }
                        try {
                            //  insert into city(cid,cname,head,pinyin) values(?,?,?,?)
                            runner.batch(sql, tempobj);//批量插入
                        } catch (Exception e) {
                        }
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }


    public  static void getCity(){
        HttpClient h=new JavaHttpClient();
        String s= null;
        try {
            s = h.doGet("http://hotels.ctrip.com/domestic-city-hotel.html",null).getResponseString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document root_document = Jsoup.parse(s);
        Elements pinyin_filter_elements = root_document.getElementsByClass("pinyin_filter_detail layoutfix");

        //包含所有城市的Element
        Element pinyin_filter = pinyin_filter_elements.first();

        //拼音首字符Elements
        Elements pinyins = pinyin_filter.getElementsByTag("dt");
        //所有dd的Elements
        Elements hotelsLinks = pinyin_filter.getElementsByTag("dd");
        for (int i = 0; i < pinyins.size(); i++) {
            Element head_pinyin = pinyins.get(i);
            Element head_hotelsLink = hotelsLinks.get(i);
            Elements links = head_hotelsLink.children();
            for (Element link : links) {
                String regEx="[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(link.attr("href"));
                String cityId =m.replaceAll("").trim();
                String cityName = link.html();
                String head_pinyin_str = head_pinyin.html();
                String pinyin_cityId = link.attr("href").replace("/hotel/", "");
                String pinyin = pinyin_cityId.replace(cityId, "");
                Object[] o=new Object[4];
                o[0]=cityId;
                o[1]=cityName;
                o[2]=head_pinyin_str;
                o[3]=pinyin;
                try {
                    insertqueue.put(o);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public  static void getJdurl(){
        QueryRunner runner = DBQuerier.getInstance().getQueryRunner();
        List<City> c=new ArrayList<>();
        try {
           c=runner.query("select * from city",new BeanListHandler<City>(City.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HttpClient h=new JavaHttpClient();
        String s= null;
        try {
            s = h.doGet("http://piao.ctrip.com/piao.html?keyword=杭州",null).getResponseString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document root_document = Jsoup.parse(s);
        Elements  e= root_document.select("head > script");

        String ja=e.toString();
        Pattern p=Pattern.compile("window.location.href='.*'");
        Matcher m=p.matcher(ja);
        if(m.find()){
            String  a=m.group();
           String t= a.replace("window.location.href=","").replace("'","");
            System.out.println("===="+a);
            System.out.println("===="+t);
        }
    }


    public  static void getJd(){
        HttpClient h=new JavaHttpClient();
        String s= null;
        try {
            s = h.doGet("http://piao.ctrip.com/piao.html?keyword=杭州",null).getResponseString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document root_document = Jsoup.parse(s);
        Elements  e= root_document.select("head > script");

        String ja=e.toString();
        Pattern p=Pattern.compile("window.location.href='.*'");
        Matcher m=p.matcher(ja);
        if(m.find()){
            String  a=m.group();
            String t= a.replace("window.location.href=","").replace("'","");
            System.out.println("===="+a);
            System.out.println("===="+t);
        }
    }
}
