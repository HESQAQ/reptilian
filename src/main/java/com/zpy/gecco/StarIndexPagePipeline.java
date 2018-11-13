package com.zpy.gecco;

import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.scheduler.SchedulerContext;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

@PipelineName("starIndexPagePipeline")
//@pipelineName 标签指定了pipline的名字。并且pipeline这个类需要实现Pipleline<T>。
public class StarIndexPagePipeline implements Pipeline<StarIndexPage> {

    @Override
    public void process(StarIndexPage starIndexPage) {

        List<StarDetail> lsStarDetail = starIndexPage.getLsStarDetail();
        //String qq=starIndexPage.getQq();

        StringBuilder inputText =  new StringBuilder();

        for (StarDetail starDetail :lsStarDetail){
            String professionHtml=starDetail.getProfessionHtml();
            String starNameHtml=starDetail.getStarNameHtml();
            Document docName= Jsoup.parse(starNameHtml);
            String starName=docName.getElementsByTag("a").attr("title").trim();

            String starSex = starDetail.getStarSex().trim();
            Document doc = Jsoup.parse(professionHtml);
            String profession="未知"; //有不含a标签的，不含a标签的都是未知的
            if(professionHtml.indexOf("<a")!= -1){
                profession = doc.getElementsByTag("a").text();
            }
            String nationality = starDetail.getNationality().trim();
            String birthday = starDetail.getBirthday().trim();
            String constellation = starDetail.getConstellation().trim();
            String height = starDetail.getHeight().trim();
            inputText.append(starName + "\t" +
                    starSex + "\t" +
                    profession + "\t" +
                    nationality + "\t" +
                    birthday + "\t" +
                    constellation + "\t" +
                    height + "\t" +
                    System.getProperty("line.separator"));
        }
        //写入文件
        writeFile(inputText.toString());

        //爬取下一页
        HttpRequest currRequest = starIndexPage.getRequest();
        int currPageNum =0;
        System.out.println("----------已爬取第"+currPageNum+"页----------");
        searchNext(currPageNum,currRequest);
    }

    //写入文档的方法
    public void writeFile(String inputText){
        try {
            if(new File("D:\\明星数据.txt").exists()){
                FileWriter fw1=new FileWriter("D:\\明星数据.txt",true);
                PrintWriter pw = new PrintWriter(fw1);
                pw.print(inputText);
                pw.flush();
                pw.close();

            }else{
                File f1 =new File("D:\\明星数据.txt");
                FileWriter fw1=new FileWriter("D:\\明星数据.txt",true);
                PrintWriter pw = new PrintWriter(fw1,true);
                pw.println("姓名"+"\t"+"性别"+"\t"+"职业"+"\t"+"国籍"+"\t"+"生日"+"\t"+"星座"+"\t"+"身高");
                pw.print(inputText);
                pw.flush();
                pw.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void searchNext(int currPageNum,HttpRequest currRequest){
        if (currPageNum<10){  //总页数只有1799
            int nextPageNum=currPageNum + 1;
            String currUrl = currRequest.getUrl();
            long a=new Date().getTime();
            String b=a+"";
            System.out.println("==="+ b.substring(0,10));
            String nextUrl = StringUtils.replaceOnce(currUrl,"page_no="+currPageNum,"page_no="+nextPageNum);
            String o="http://ent.sina.com.cn/ku/star_search_index.d.html?page="+nextPageNum+"&cTime="+ b.substring(0,10)+"&pre=next";
            SchedulerContext.into(currRequest.subRequest(o));
        }
        else{
            System.out.println("---------------爬取完毕------------------");
        }

    }
}
