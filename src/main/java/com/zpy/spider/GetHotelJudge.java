package com.zpy.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.http.HttpMethod;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.plaf.metal.OceanTheme;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;

/**
 * 获取携程酒店的评价信息
 * Created by Administrator on 2018/9/28.
 */
public class GetHotelJudge {
    private static BlockingQueue<Object[]> insertqueuej= new LinkedBlockingQueue<>();//评价

    public static void main(String[] args) throws IOException {

        //String sql="insert into hoteljude(HOTELNAME,USERIMG,USERNAME,SCORE,GGSCORE,HOTELTYPE,CHECKINTIME,BADTYPE,JUDGETIME,JUDGEINTRO,JUDGEREPLAY,JUDGEIMG) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        //URLDemo2.insert(sql,insertqueuej);

        getHotel();

    }

    public static void   getHotel(){

        try {
            String hotelid="441351"; //酒店id
            String uri="http://hotels.ctrip.com/hotel/"+hotelid+".html";
            HttpClient h=new JavaHttpClient();
            HttpResponse  s = h.doGet(uri,null);
            String hotel=s.getResponseString();
            Document doc=Jsoup.parse(hotel);//获取酒店信息
            Element e=doc.selectFirst("#J_htl_info > div.name > h2.cn_n");
            String hotelname=e.text();
            Map cookies=s.getCookies();
            Map head=gethead(hotelid);
            String even=oceanball(hotelid,head,cookies);//获取even

            /*String url="http://hotels.ctrip.com/Domestic/tool/AjaxHote1RoomListForDetai1.aspx?psid=&MasterHotelID=441351&hotel=441351&EDM=F&roomId=&IncludeRoom=&city=2&showspothotel=T&supplier=&IsDecoupleSpotHotelAndGroup=F&contrast=0&brand=0&startDate=2018-10-15&depDate=2018-10-16&IsFlash=F&RequestTravelMoney=F&hsids=&IsJustConfirm=&contyped=0&priceInfo=-1&equip=&filter=&productcode=&couponList=&abForHuaZhu=&defaultLoad=T&esfiltertag=&estagid=&TmFromList=F&RoomGuestCount=1,1,0&eleven="+even+"&callback="+getcallback(15)+"&_="+System.currentTimeMillis()+"";
            HttpResponse res=h.doGet2(url,head,cookies);
            String room=res.getResponseString();//获取房间的json数据*/
            int currentPage=0;
            getEiinfo(currentPage,hotelid,"",head,even,cookies,hotelname);//获取评价信息


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //获取评价信息
    public static void getEiinfo(int currentPage,String hotelid,String sub,Map headMap,String eleven,Map cookies, String hotelname){
        System.out.println("===================================currentPage:"+currentPage);
        try {
            String pl = "http://hotels.ctrip.com/Domestic/tool/AjaxHotelCommentList.aspx?" +
                    "MasterHotelID=" + hotelid + "&hotel=" + hotelid + "&NewOpenCount=0&AutoExpiredCount=0&RecordCount=2365&OpenDate=&keywordPress=1&card=-1&property=-1" +
                    "&UserType=&productcode=&keyword=&roomName=&orderBy=2&currentPage="+currentPage+"&viewVersion=c&contyped=0" +
                    "&eleven="+eleven+"&callback="+getcallback(15)+"&_="+System.currentTimeMillis();
            HttpClient h=new JavaHttpClient();
            HttpResponse r=h.doGet2(pl,headMap,cookies);
            String result=r.getResponseString();
            Document doc=Jsoup.parse(result);
            Element element= doc.selectFirst("#divCtripComment > div.comment_detail_list");//得到所有的评论信息div
            if(element!=null){
                Elements elements=element.getElementsByClass("comment_block J_asyncCmt");
                if(elements!=null&&elements.size()>0){
                    for (Element e:elements){
                        Element eltImg=e.selectFirst("div.user_info > p.head > span.img > img");
                        String img=eltImg==null?null:eltImg.attr("src");//用户头像
                        //System.out.println("img:"+img);
                        Element eltName=e.selectFirst("div.user_info > p.name > span");
                        String name=eltName==null?null:eltName.text();//用户名称
                        //System.out.println("name:"+name);
                        Element eltscore=e.selectFirst("div.comment_main > p > span.score > span");
                        String score=eltscore==null?0+"":eltscore.text();//评分总分
                        //System.out.println("score:"+score);
                        Element eltggscore=e.selectFirst("div.comment_main > p > span.small_c");
                        String ggscore=eltggscore==null?null:eltggscore.attr("data-value");//各个维度评分
                        //System.out.println("ggscore:"+ggscore);
                        Element elttype=e.selectFirst("div.comment_main > p > span.type");
                        String type=elttype==null?null:elttype.text();//出游类型
                        //System.out.println("type:"+type);
                        Element elttime=e.selectFirst("div.comment_main > p > span.date");
                        String time=elttime==null?null:elttime.text();//出游时间
                        // System.out.println("time:"+time);
                        Element eltbad=e.selectFirst("div.comment_main > p > a");
                        String bad=eltbad==null?null:eltbad.text();//房型
                        //System.out.println("bad:"+bad);
                        Element eltdate=e.selectFirst("div.comment_main > div.comment_txt > div.comment_bar > p > span");
                        String date=eltdate==null?null:eltdate.text().replace("发表于","");//评论时间
                        //System.out.println("date:"+date);
                        Element eltintro=e.selectFirst("div.comment_main > div.comment_txt > div.J_commentDetail");
                        String intro=eltintro==null?null:eltintro.text();//评论内容
                        System.out.println("intro:"+intro);
                        Element eltreplay=e.selectFirst("div.comment_main > div.htl_reply > p.text");
                        String replay=eltreplay==null?null:eltreplay.text();//酒店回复内容
                        //System.out.println("replay:"+replay);
                        Element picturediv=e.selectFirst("div.comment_main > div > div.comment_pic");
                        String purl="";
                        if(picturediv!=null){
                            Elements picture=picturediv.getElementsByClass("pic");//评论的图集
                            if(picture!=null&&picture.size()>0){
                                for (Element pic:picture){
                                    Element imgs=pic.selectFirst("img.p");
                                    String url=imgs==null?null:imgs.attr("src");
                                    purl=purl+url+";";
                                    //System.out.println("url:"+url);
                                }
                            }
                        }

                        //组装成对象
                        Object[] o=new Object[12];
                        o[0]=hotelname;
                        o[1]=img;
                        o[2]=name;
                        o[3]=score;
                        o[4]=ggscore;
                        o[5]=type;
                        o[6]=time;
                        o[7]=bad;
                        o[8]=date;
                        o[9]=intro;
                        o[10]=replay;
                        o[11]=purl;
                        insertqueuej.add(o);//加入队列进行入库

                    }
                }
                Elements adiv=doc.select("#divCtripComment > div.c_page_box > div > div.c_page_list.layoutfix > a");//获取分页信息的a标签
                int tpage=Integer.parseInt(adiv.last().text());//得到总页数的值
                Element cPage=doc.selectFirst("#divCtripComment > div.c_page_box > div > div.c_page_list.layoutfix > a.current");//得到当前页的a标签
                int cpage=Integer.parseInt(cPage.text());//得到当前页的值
                if (cpage+1<=tpage){//进行循环读取
               /* try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                    String even=oceanball(hotelid,headMap,cookies);//获取even
                    getEiinfo(cpage+1,hotelid,"",headMap,even,cookies,hotelname);//获取评价信息
                }

            }else{
                String even=oceanball(hotelid,headMap,cookies);//获取even
                getEiinfo(currentPage,hotelid,"",headMap,even,cookies,hotelname);//获取评价信息
            }


            } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 请求头设置
     * @param hotelid
     * @return
     */
    public static  Map gethead(String hotelid){
        Map map = new HashMap();
        map.put("Host", "hotels.ctrip.com");

        map.put("Accept", "*/*");
        map.put("Cache-Control", "max-age=0");
        map.put("If-Modified-Since", "Thu, 01 Jan 1970 00:00:00 GMT");
        map.put("Content-Type","application/x-javascript; charset=utf-8");
        map.put("Accept-Language", "zh-CN,zh;q=0.8");
        map.put("Referer", "http://hotels.ctrip.com/hotel/" + hotelid + ".html");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
        return  map;
    }

    /**
     *  加密
     * @param hotelid
     * @return
     */
    public static String oceanball(String hotelid,Map headMap,Map cookies){

        try {
            String callback = getcallback(15);
            HttpClient h=new JavaHttpClient();
            long currtime = System.currentTimeMillis();
            String oceanball = "http://hotels.ctrip.com/domestic/cas/oceanball?callback="+callback+"&_="+currtime+"";
            HttpResponse res=h.doGet2(oceanball,headMap,cookies);
            String ocean = res.getResponseString();
            ocean = ocean.replace("eval","JSON.stringify");
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            ocean = String.valueOf( engine.eval(ocean));
            ocean = ocean.replace(callback,"var eleven=" + callback);
            ocean = String.valueOf(engine.eval(new StringReader(ocean)));
            ScriptEngineManager manager1 = new ScriptEngineManager();
            ScriptEngine engine1 = manager1.getEngineByName("javascript");

            engine1.eval("var hotel_id = \""+hotelid+"\"; var site = {}; site.getUserAgent = function(){}; var Image = function(){}; var window = {}; window.document = window.document = {body:{innerHTML:\"1\"}, documentElement:{attributes:{webdriver:\"1\"}}, createElement:function(x){return {innerHTML:\"1\"}}}; var document = window.document;window.navigator = {\"appCodeName\":\"Mozilla\", \"appName\":\"Netscape\", \"language\":\"zh-CN\", \"platform\":\"Win\"}; window.navigator.userAgent = site.getUserAgent(); var navigator = window.navigator; window.location = {}; window.location.href = \"http://hotels.ctrip.com/hotel/\"+hotel_id+\".html\"; var location = window.location;" +
                    " var navigator = {userAgent:{indexOf: function(x){return \"1\"}}, geolocation:\"1\"};var getEleven = 'zgs';  " );
            engine1.eval("var "+callback+" = function(a){getEleven = a;};");
            engine1.eval(ocean);
            String eleven = "";
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine1;
                eleven = (String) invocable.invokeFunction("getEleven");//4.使用 invocable.invokeFunction掉用js脚本里的方法，第一個参数为方法名，后面的参数为被调用的js方法的入参
            }
            return eleven;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * callback参数获取
     * @param number
     * @return
     */
    public  static String getcallback(int number){
        String s[]={"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String cal="CAS";
        for(int i=0;i<number;i++){
            int t= (int) Math.ceil(51 * Math.random());
            cal=cal+s[t];
        }
       return cal;
    }
}
