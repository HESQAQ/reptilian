package com.zpy.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zpy.dao.dataobject.Detail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**爬取门票评价信息
 * Created by Administrator on 2018/8/23.
 */
public class getJudge {
    private static BlockingQueue<Object[]> insertqueuej= new LinkedBlockingQueue<>();//评价
    public static void main(String[] args) throws IOException, ParseException {
        String sql="insert into judgeinfo(id,CONTENT,JDATE,SCORE,PLAYDATE,SNAME,USID) values(?,?,?,?,?,?,?)";
        URLDemo2.insert(sql,insertqueuej);

        Map dateMap=new HashMap();
        dateMap.put("contentType","json");
        dateMap.put("pageid","10650000804");
        dateMap.put("pagenum","0");
        dateMap.put("pagesize","0");
        Map head=new HashMap();
        head.put("appid","100013776");
        head.put("auth","");
        head.put("ctok","");
        head.put("cver","");
        head.put("extension",new ArrayList<>());
        head.put("lang","01");
        head.put("sid","8888");
        head.put("syscode","09");
        dateMap.put("head",head);
        dateMap.put("tagid","0");
        dateMap.put("ver","7.10.3.0319180000");
        dateMap.put("viewid","48020");
        String ss=JSON.toJSONString(dateMap);
        HttpClient h=new JavaHttpClient();
        Map headers=new HashMap();
        headers.put("Content-Type","application/json");

        String s = h.dopost("http://sec-m.ctrip.com/restapi/soa2/12530/json/viewCommentList",headers,new ByteArrayInputStream(ss.toString().getBytes()),null).getResponseString();
        JSONObject j=JSON.parseObject(s);
        JSONObject da=j.getJSONObject("data");
        String cmtquantity= da.getString("cmtquantity");
        String cmtscore= da.getString("cmtscore");
        String recompct= da.getString("recompct");
        int totalpage= da.getIntValue("totalpage");
        List<Detail> r = JSON.parseArray(da.getString("comments"), Detail.class);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 =new SimpleDateFormat("yyyy-MM-dd");
        for(int i=1;i<=totalpage;i++){
            System.out.println("i======"+i);
            dateMap.put("pagenum",i);
            dateMap.put("pagesize",r.size());
            ss=JSON.toJSONString(dateMap);
            String su = h.dopost("http://sec-m.ctrip.com/restapi/soa2/12530/json/viewCommentList",headers,new ByteArrayInputStream(ss.toString().getBytes()),null).getResponseString();
            JSONObject ju=JSON.parseObject(su);
            JSONObject dau=ju.getJSONObject("data");
            List<Detail> ru = JSON.parseArray(dau.getString("comments"), Detail.class);
            System.out.println("size==="+ru.size());
            for(Detail d:ru){
                //组装成对象
                Object[] judge=new Object[7];
                judge[0]=d.getId();
                judge[1]=d.getContent();
                judge[2]=d.getDate();
                judge[3]=d.getScore();
                judge[4]=d.getPlaydate();
                judge[5]="杭州灵隐（飞来峰）景区";
                judge[6]=d.getUid();
                insertqueuej.add(judge);//加入队列进行入库
            }
        }


    }


}
