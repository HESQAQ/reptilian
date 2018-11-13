package com.zpy.spider;

import com.zpy.QButil.DBQuerier;
import org.apache.commons.dbutils.QueryRunner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLDemo2 {
    //提取的数据存放到该目录下
    //等待爬取的url
    private static List<String> allwaiturl=new ArrayList<>();
    //爬取过的url
    private static Set<String> alloverurl=new HashSet<>();
    //记录所有url的深度进行爬取判断
    private static Map<String,Integer> allurldepth=new HashMap<>();
    //爬取得深度
    private static int maxdepth=2;
    //生命对象，帮助进行线程的等待操作
    private static Object obj=new Object();
    //记录总线程数5条
    private static int MAX_THREAD=1;
    //记录空闲的线程数
    private static int count=0;
    private static BlockingQueue<Object[]> insertqueue2 = new LinkedBlockingQueue<>();//景点
    public static void main(String args[]){
        String strurl="http://piao.ctrip.com/dest/u-_ba_bc_d6_dd/s-tickets/P1/";
        //启动入库的操作
        insert("insert into SCENICINFO(name,address,score,judenum,city,grade,special,link) values(?,?,?,?,?,?,?,?)",insertqueue2);
        addurl(strurl,0);
        for(int i=0;i<MAX_THREAD;i++){
            new URLDemo2().new MyThread().start();
        }
    }
    /**
     * 网页数据爬取
     * @param strurl
     *
     * @param depth
     */
    public static void workurl(String strurl,int depth){
        //判断当前url是否爬取过
        //if(!(alloverurl.contains(strurl)||depth>maxdepth)){
        if(!(alloverurl.contains(strurl))){
            //检测线程是否执行
            System.out.println("当前执行："+Thread.currentThread().getName()+" 爬取线程处理爬取："+strurl);
            //建立url爬取核心对象
            try {
                HttpClient h=new JavaHttpClient();
                String s = h.doGet(strurl,null).getResponseString();
                Document root_document = Jsoup.parse(s);
                //获取需要数据的div
                Element e= root_document.getElementById("searchResultContainer");
                //得到网页上的景点列表（样式包含04与05）
                Elements yy = e.getElementsByClass("searchresult_product04");
                Elements yy2 = e.getElementsByClass("searchresult_product05");
                yy.addAll(yy2);
                System.out.println("-------------------------------------"+yy.size());
                for(int i=0;i<yy.size();i++){
                    //得到每一条景点信息
                    Element Info=yy.get(i);
                    //分析网页得到景点的标题（使用选择器语法来查找元素）
                    Element nameStr=Info.selectFirst("div > div.search_ticket_title > h2 > a");
                    String name=nameStr.html();//得到标题的信息
                    String link=nameStr.attr("href");//得到标题的链接（可以存库对详情信息获取）
                    Element gradeStr=Info.selectFirst(" div > div.search_ticket_title > h2 > span > span.rate");//得到景点等级
                    String  grade=gradeStr.html();
                    Element addressStr=Info.selectFirst("div > div.search_ticket_title > div.adress");//得到景点地址
                    String address=addressStr.html();
                    Element scoreStr=Info.selectFirst("div > div.search_ticket_assess > span.grades > em");//得到评分
                    String score=null;
                    if(scoreStr!=null){
                        score=scoreStr.html();
                    }
                    Element judenumber=Info.selectFirst("div > div.search_ticket_assess > span.grades");//得到评论人数
                    String judenum="";
                    if(judenumber!=null){
                        judenum=judenumber.text();//----分(3580人点评)
                        //使用正则表达式抽取评论人数
                        Pattern p=Pattern.compile("\\(.*人点评\\)");
                        Matcher m=p.matcher(judenum);
                        if(m.find()){
                            judenum=m.group();
                        }
                    }
                    Element specialStr=Info.selectFirst("div > div.search_ticket_title > div.exercise");//得到特色
                    String special=specialStr.html();
                    grade=("").equals(grade)?null:grade;
                    //获取的地址是这样的 ----》地址：浙江省湖州市安吉县递铺镇天使大道1号  所以需要进行截取操作
                    address=("").equals(address)?null:address.substring(address.indexOf("：")+1);
                    special=("").equals(special)?null:special.substring(special.indexOf("：")+1);
                    judenum=("").equals(judenumber)?null:Utils.getNum(judenum);
                    System.out.println("==="+name);

                    //组装成对象
                    Object[] o=new Object[8];
                    o[0]=name;
                    o[1]=address;
                    o[2]=score;
                    o[3]=judenum;
                    o[4]="杭州";
                    o[5]=grade;
                    o[6]=special;
                    o[7]=link;
                    insertqueue2.add(o);//加入队列进行入库
                }
                Elements totalPage=e.select("div.pkg_page.basefix > a");//获取分页信息的a标签
                int tpage=Integer.parseInt(totalPage.get(totalPage.size()-2).html());//得到总页数的值
                Element currentPage=e.selectFirst("div.pkg_page.basefix > a.current");//得到当前页的a标签
                int cpage=Integer.parseInt(currentPage.html());//得到当前页的值
                if (cpage+1<=tpage){//进行循环读取
                    addurl("http://piao.ctrip.com/dest/u-_ba_bc_d6_dd/s-tickets/P"+(cpage+1)+"/",depth);//进行循环读取
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //将当前url归列到alloverurl中
            alloverurl.add(strurl);
            System.out.println(strurl+"网页爬取完成，已爬取数量："+alloverurl.size()+"，剩余爬取数量："+allwaiturl.size());
        }
        if(allwaiturl.size()>0){
            synchronized(obj){
                obj.notify();
            }
        }else{
            System.out.println("爬取结束.......");
        }

    }
    /**
     * 将获取的url放入等待队列中，同时判断是否已经放过
     * @param href
     * @param depth
     */
    public static synchronized void addurl(String href,int depth){
        //将url放到队列中
        allwaiturl.add(href);
        //判断url是否放过
        if(!allurldepth.containsKey(href)){
            allurldepth.put(href, depth+1);
        }
    }
    /**
     * 移除爬取完成的url，获取下一个未爬取得url
     * @return
     */
    public static synchronized String geturl(){
        String nexturl=allwaiturl.get(0);
        allwaiturl.remove(0);
        return nexturl;
    }


    public static void  insert(String sql,BlockingQueue<Object[]>  queue){ //开启一个插入数据的线程
        new Thread(){
            @Override
            public void run() {
                while(true){
                    if(queue.size()>0){
                        QueryRunner runner = DBQuerier.getInstance().getQueryRunner();//使用c3p0进行连接dbutils
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

    /**
     * 线程分配任务
     */
class MyThread extends Thread{
        @Override
        public void run(){
            //设定一个死循环，让线程一直存在
            while(true){
                //判断是否新链接，有则获取
                if(allwaiturl.size()>0){
                    //获取url进行处理
                    String url=geturl();
                    //调用workurl方法爬取
                    workurl(url,allurldepth.get(url));
                }else{
                    System.out.println("当前线程准备就绪，等待连接爬取："+this.getName());
                    count++;
                    //建立一个对象，让线程进入等待状态，即wait（）
                    synchronized(obj){
                        try{
                            obj.wait();
                        }catch(Exception e){

                        }
                    }
                    count--;
                }
            }
        }

    }
}
