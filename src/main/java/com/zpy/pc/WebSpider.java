package com.zpy.pc;

import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebSpider {
    public static void main(String[] args) {
        URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;
       // PrintWriter pw = null;

        try {
            //通过url建立与网页的连接
            url = new URL("http://www.baidu.com/");
            urlconn = url.openConnection();
            //pw = new PrintWriter(new FileWriter("f:/url(baidu).txt"), true);//这里我把爬到的链接存储在了F盘底下的一个叫做url（baidu）的doc文件中
            //通过链接取得网页返回的数据
            br = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            String buf = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((buf = br.readLine()) != null) {
                stringBuffer.append(buf);
            }
            System.out.println("获取成功！"+stringBuffer);
            //编写获取http链接的正则表达式
            String regex = "http://[\\w+\\.?/?]+\\.[A-Za-z]+";
            Pattern p = Pattern.compile(regex);
            //匹配返回的结果
            Matcher m=p.matcher(stringBuffer.toString());
            while(m.find()){//查找匹配的链接
                String a=m.group();
                System.out.println("====爬取的链接"+a);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
           // pw.close();
        }
    }
}
