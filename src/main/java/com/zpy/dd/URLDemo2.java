package com.zpy.dd;

        import java.io.*;
        import java.net.*;
        import java.util.*;
        import java.util.regex.*;

public class URLDemo2 {
    //提取的数据存放到该目录下
    private static String savepath="E:/dangdang_book/";
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
    private static int MAX_THREAD=7;
    //记录空闲的线程数
    private static int count=0;

    public static void main(String args[]){
        //确定爬取的网页地址，此处为当当网首页上的图书分类进去的网页
        //网址为        http://book.dangdang.com/
//        String strurl="http://search.dangdang.com/?key=%BB%FA%D0%B5%B1%ED&act=input";
        String strurl="http://book.dangdang.com/";

        //workurl(strurl,1);
        addurl(strurl,0);
        for(int i=0;i<MAX_THREAD;i++){
            new URLDemo2().new MyThread().start();
        }
    }
    /**
     * 网页数据爬取
     * @param strurl
     * @param depth
     */
    public static void workurl(String strurl,int depth){
        //判断当前url是否爬取过
        if(!(alloverurl.contains(strurl)||depth>maxdepth)){
            //检测线程是否执行
            System.out.println("当前执行："+Thread.currentThread().getName()+" 爬取线程处理爬取："+strurl);
            //建立url爬取核心对象
            try {
                URL url=new URL(strurl);
                //通过url建立与网页的连接
                URLConnection conn=url.openConnection();
                //通过链接取得网页返回的数据
                InputStream is=conn.getInputStream();

                //提取text类型的数据
                if(conn.getContentType().startsWith("text")){

                }
                System.out.println(conn.getContentEncoding());
                //一般按行读取网页数据，并进行内容分析
                //因此用BufferedReader和InputStreamReader把字节流转化为字符流的缓冲流
                //进行转换时，需要处理编码格式问题
                BufferedReader br=new BufferedReader(new InputStreamReader(is,"GB2312"));

                //按行读取并打印
                String line=null;
                //正则表达式的匹配规则提取该网页的链接
                //http://product.dangdang.com/25305374.html
                //Pattern p=Pattern.compile("<a .*href='http://product.dangdang.com/*.html'.+</a>");
                Pattern p=Pattern.compile("<a .*href=.+</a>");
                //建立一个输出流，用于保存文件,文件名为执行时间，以防重复
                PrintWriter pw=new PrintWriter(new File(savepath+System.currentTimeMillis()+".txt"));

                while((line=br.readLine())!=null){
                    //System.out.println(line);
                    //编写正则，匹配超链接地址
                    pw.println(line);
                    Matcher m=p.matcher(line);
                    while(m.find()){
                        String href=m.group();
                        //System.out.println("================"+href);
                        //找到超链接地址并截取字符串
                        //有无引号
                        href=href.substring(href.indexOf("href="));
                        if(href.charAt(5)=='\"'){
                            href=href.substring(6);
                        }else{
                            href=href.substring(5);
                        }
                        //截取到引号或者空格或者到">"结束
                        try{
                            href=href.substring(0,href.indexOf("\""));
                        }catch(Exception e){
                            try{
                                href=href.substring(0,href.indexOf(" "));
                            }catch(Exception e1){
                                href=href.substring(0,href.indexOf(">"));
                            }
                        }
                        if(href.startsWith("http://product.dangdang.com/")&&href.endsWith(".html")){
                            System.out.println("========"+href);
                            Pattern pp=Pattern.compile("http://product.dangdang.com/*.html");
                            Matcher mn=pp.matcher(href);
                            if(mn.find()){
                                String href1=m.group();
                                System.out.println("---------------"+href1);
                            }
                    /*
                    //输出该网页存在的链接
                    //System.out.println(href);
                    //将url地址放到队列中
                    allwaiturl.add(href);
                    allurldepth.put(href,depth+1);
                    */
                            //调用addurl方法

                            addurl(href,depth);
                        }

                    }

                }
                pw.close();
                br.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
            //将当前url归列到alloverurl中
            alloverurl.add(strurl);
            System.out.println(strurl+"网页爬取完成，已爬取数量："+alloverurl.size()+"，剩余爬取数量："+allwaiturl.size());
        }
        /*
        //用递归的方法继续爬取其他链接
        String nexturl=allwaiturl.get(0);
        allwaiturl.remove(0);
        workurl(nexturl,allurldepth.get(nexturl));
        */
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
