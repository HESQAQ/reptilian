package com.zpy.httputils;

/**
 * urlconnection
 * Created by Administrator on 2018/7/31.
 */
import org.apache.commons.httpclient.Header;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ConnectionUtil {

    public static String Connect(String address){
        HttpURLConnection conn = null;
        URL url = null;
        InputStream in = null;
        BufferedReader reader = null;
        StringBuffer stringBuffer = null;
        try {
            //1.新建url对象，表示要访问的网页
            url = new URL(address);
            //2.建立http连接,返回连接对象urlconnection
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.connect();

            //3.获取相应的http状态码，
            int code=conn.getResponseCode();
            if(code!=200){
                return null;
            }
            //4.如果获取成功，从URLconnection对象获取输入流来获取请求网页的源代码
            in = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            stringBuffer = new StringBuffer();
            String line = null;
            while((line = reader.readLine()) != null){
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            conn.disconnect();
            try {
                in.close();
                reader.close();
            } catch (Exception e) {
                System.out.println("获取不到网页源码："+e);
                e.printStackTrace();
            }
        }

        return stringBuffer.toString();
    }



    public static String doPost(String httpUrl, String param) throws IOException {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection(); // 通过远程url连接对象打开连接
            connection.setRequestMethod("POST"); // 设置连接请求方式
            connection.setConnectTimeout(15000); // 设置连接主机服务器超时时间：15000毫秒
            connection.setReadTimeout(60000);   // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setUseCaches(false);   // post请求缓存设为false
            //connection.setInstanceFollowRedirects(true); // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setDoOutput(true);// 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoInput(true);// 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
            // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
            connection.connect(); // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            os = connection.getOutputStream(); // 通过连接对象获取一个输出流
            os.write(param.getBytes());   // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
           // if (connection.getResponseCode() == 200) {  // 通过连接对象获取一个输入流，向远程读取
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));  // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();

                String sessionId = "";
                String cookieVal = "";
                String key = null;
                for(int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++){
                    if(key.equalsIgnoreCase("set-cookie")){
                        cookieVal = connection.getHeaderField(i);
                        cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                        System.out.println("==="+cookieVal);
                        sessionId = sessionId + cookieVal + ";";
                    }
                }

                return sessionId;

           // }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }



    public static String doPost2(String httpUrl, String param, String cookies,Map<String, String> userHeaders) throws IOException {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            if(cookies!=null){
                connection.addRequestProperty("Cookie", cookies);
            }
            if (userHeaders != null) {
                for (Map.Entry<String, String> entry : userHeaders.entrySet()) {
                    connection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            os = connection.getOutputStream();
            os.write(param.getBytes());
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                String cookieVal = "";
                String key = null;
                for(int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++){
                    if(key.equalsIgnoreCase("set-cookie")){//
                        cookieVal = connection.getHeaderField(i);
                        cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                        System.out.println("==="+cookieVal);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }


    public static void main(String[] args){
        Connect("");
    }

}
