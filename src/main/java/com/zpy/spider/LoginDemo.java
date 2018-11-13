package com.zpy.spider;

import com.zpy.httputils.HttpClient3;
import org.apache.http.HttpHeaders;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/9.
 */
public class LoginDemo {
    /**
     * 模拟登陆CSDN  使用jsoup
     *
     * @param userName
     *            用户名
     * @param pwd
     *            密码
     *
     * **/
    public Map<String,String>  login(String userName, String pwd) throws Exception  {
        // 第一次请求
        Connection con = Jsoup.connect("https://passport.csdn.net/account/login");// 获取连接
        con.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// 配置模拟浏览器
        Response rs = con.execute();// 获取响应
        Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
        List<Element> et = d1.select("#fm1");// 获取form表单，可以通过查看页面源码代码得知
        // 获取，cooking和表单属性，下面map存放post时的数据
        Map<String, String> datas = new HashMap<>();
        for (Element e : et.get(0).getAllElements()) {
            if (e.attr("name").equals("username")) {
                e.attr("value", userName);// 设置用户名
            }
            if (e.attr("name").equals("password")) {
                e.attr("value", pwd); // 设置用户密码
            }
            if (e.attr("name").length() > 0) {// 排除空值表单属性
                datas.put(e.attr("name"), e.attr("value"));
            }
        }

        System.out.println("======map"+datas);
        System.out.println("======"+rs.cookies());
        /**
         * 第二次请求，post表单数据，以及cookie信息
         *
         * **/
        Connection con2 = Jsoup.connect("https://passport.csdn.net/account/login");
        con2.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        // 设置cookie和post上面的map数据
        Response login = con2.ignoreContentType(true).method(Method.POST).data(datas).cookies(rs.cookies()).execute();
        // 打印，登陆成功后的信息
        System.out.println(login.body());
        // 登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可
        Map<String, String> cookies = login.cookies();
        for (String s : cookies.keySet()) {
            System.out.println(s + "      " + cookies.get(s));
        }

        return cookies;
    }


    /**
     * 模拟登陆CSDN  使用HttpURLConnection
     *
     * @param userName
     *            用户名
     * @param pwd
     *            密码
     *
     * **/
    public Map<String,String>  loginbyhttpurl(String userName, String pwd) throws Exception  {

        //设置请求头
        Map<String,String> hea=new HashMap();
        //模拟浏览器请求
        hea.put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");

        HttpClient h=new JavaHttpClient();
        HttpResponse re = h.doGet("https://passport.csdn.net/account/login",hea);//第一次请求
        int code=re.getResponseCode();
        String w=re.getResponseString();
        Map<String ,String> cookie=re.getCookies();

        Document root=Jsoup.parse(w);//解析第一次的页面得到需要登录的参数
        Element form=root.selectFirst("#fm1");
        String lt = form.select("input[name=lt]").get(0).val();
        String execution = form.select("input[name=execution]").get(0).val();
        String _eventId = form.select("input[name=_eventId]").get(0).val();
        String fkid = form.select("input[name=fkid]").get(0).val();
        String gps = form.select("input[name=gps]").get(0).val();

        Map param=new HashMap();
        param.put("username","");
        param.put("password","");
        param.put("lt",lt);
        param.put("execution",execution);
        param.put("rememberMe","true");
        param.put("gps",gps);
        param.put("fkid",fkid);
        param.put("_eventId",_eventId);

        //组装请求参数
        StringBuffer params = new StringBuffer();
        // 表单参数与get形式一样
        params.append("username").append("=").append(userName).append("&")
                .append("password").append("=").append(pwd).append("&")
                .append("lt").append("=").append(lt).append("&")
                .append("execution").append("=").append(execution).append("&")
                .append("_eventId").append("=").append(_eventId).append("&")
                .append("rememberMe").append("=").append("true").append("&")
                .append("gps").append("=").append(gps).append("&")
                .append("fkid").append("=").append(fkid);
        InputStream is = new ByteArrayInputStream(params.toString().getBytes());

        String result=HttpClient3.doPost("https://passport.csdn.net/account/login",param,hea,cookie);
        HttpResponse uu=h.dopost("https://passport.csdn.net/account/login",hea,is,cookie);//模拟登录
        System.out.println("===="+uu.getResponseCode());//登录成功
        System.out.println("===="+uu.getCookies());//返回cookies信息
        System.out.println("===="+uu.getResponseString());

        return uu.getCookies();
    }

    public static void main(String[] args) throws Exception {
        LoginDemo loginDemo = new LoginDemo();
        /*Map cookies=loginDemo.login("XXXXX", "XXX.");// 输入CSDN的用户名，和密码登录后获得cookies
       //请求一个需登录的页面，传入cookie
        Document doc = Jsoup.connect("https://mp.csdn.net/postedit/79928429")
                .userAgent("Mozilla")
                .cookies(cookies)
                .timeout(3000)
                .get();*/

        Map cookies2=loginDemo.loginbyhttpurl("", "");
        //使用返回的cookies信息进行访问需要登录才可以获取数据的页面
        HttpClient h=new JavaHttpClient();
        String s = h.doGet2("https://mp.csdn.net/postedit/79928429",null,cookies2).getResponseString();



    }
}
