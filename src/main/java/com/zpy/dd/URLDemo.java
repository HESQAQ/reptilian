package com.zpy.dd;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class URLDemo {
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

    public static void main(String args[]){
        //确定爬取的网页地址，此处为当当网首页上的图书分类进去的网页
        //网址为        http://book.dangdang.com/
//        String strurl="http://search.dangdang.com/?key=%BB%FA%D0%B5%B1%ED&act=input";
        String strurl="http://book.dangdang.com/";

        workurl(strurl,1);

    }
    public static void workurl(String strurl,int depth){
        //判断当前url是否爬取过
        if(!(alloverurl.contains(strurl)||depth>maxdepth)){
            //建立url爬取核心对象
            try {
                URL url=new URL(strurl);
                //通过url建立与网页的连接
                URLConnection conn=url.openConnection();
                //通过链接取得网页返回的数据
                InputStream is=conn.getInputStream();

                System.out.println(conn.getContentEncoding());
                //一般按行读取网页数据，并进行内容分析
                //因此用BufferedReader和InputStreamReader把字节流转化为字符流的缓冲流
                //进行转换时，需要处理编码格式问题
                BufferedReader br=new BufferedReader(new InputStreamReader(is,"GB2312"));

                //按行读取并打印
                String line=null;
                //正则表达式的匹配规则提取该网页的链接
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
                        if(href.startsWith("http:")||href.startsWith("https:")){
                            //输出该网页存在的链接
                            //System.out.println(href);
                            //将url地址放到队列中
                            allwaiturl.add(href);
                            allurldepth.put(href,depth+1);
                        }

                    }

                }
                pw.close();
                br.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //将当前url归列到alloverurl中
            alloverurl.add(strurl);
            System.out.println(strurl+"网页爬取完成，已爬取数量："+alloverurl.size()+"，剩余爬取数量："+allwaiturl.size());
        }
        //用递归的方法继续爬取其他链接
        String nexturl=allwaiturl.get(0);
        allwaiturl.remove(0);
        workurl(nexturl,allurldepth.get(nexturl));
    }
}
