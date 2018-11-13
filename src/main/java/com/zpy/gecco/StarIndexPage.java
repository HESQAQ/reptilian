package com.zpy.gecco;

import com.geccocrawler.gecco.annotation.*;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;
import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/7/31.
 */

//http://ent.sina.com.cn/ku/star_search_index.d.html?page=2&cTime=1532913461&pre=next
@Data
//@Gecco(matchUrl = "http://ent.sina.com.cn/ku/star_search_index.d.html?page={page}&cTime={cTime}&pre=next",pipelines={"consolePipeline","starIndexPagePipeline"})
@Gecco(matchUrl = "http://ent.sina.com.cn/ku/star_search_index.d.html",pipelines={"consolePipeline","starIndexPagePipeline"})
//matchUrl是爬取相匹配的url路径，然后将获取到的HtmlBean输出到相应的管道（pipelines）进行处理。这里的管道是可以自定义的。
public class StarIndexPage implements HtmlBean {

    private static final long serialVersionUID = 1225018257932399804L;

    @Request
    private HttpRequest request;

    //url中的page参数
    @RequestParameter("page")
    private String  page;

    //url中的page参数
    @RequestParameter("cTime")
    private String  cTime;

    //首页中的明星板块的集合，li的集合
    @HtmlField(cssPath ="#dataListInner")
    private List<StarDetail> lsStarDetail;
//@HtmlField(cssPath = "#dataListInner > ul >li")是用来抓取网页中的相应网页数据，csspath是jQuery的形式。
//cssPath获取小技巧：用Chrome浏览器打开需要抓取的网页，按F12进入发者模式。然后在浏览器右侧选中该元素，鼠标右键选择Copy–Copy selector，即可获得该元素的cssPath

    //当前的页码，如果当前的是有很多页码的话，可以通过获取当前页码还有总页码，为继续抓取下一页做准备
//@Text是指抓取网页中的文本部分。@Html是指抓取Html代码。@Href是用来抓取元素的连接 @Ajax是指获取Ajax得到的内容。
   @Text
    @HtmlField(cssPath = "#dataListInner > div > ul > li.curr a")
    private String currPageNum;

    /*@Text
    @HtmlField(cssPath ="#dataListInner > ul > li:nth-child(1) > div > p:nth-child(2)")
    private String sex;

    @HtmlField(cssPath = "#dataListInner > ul > li:nth-child(1) > div > p:nth-child(2)")
    private  String ww;*/


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public List<StarDetail> getLsStarDetail() {
        return lsStarDetail;
    }

    public void setLsStarDetail(List<StarDetail> lsStarDetail) {
        this.lsStarDetail = lsStarDetail;
    }

    public String getCurrPageNum() {
        return currPageNum;
    }

    public void setCurrPageNum(String currPageNum) {
        this.currPageNum = currPageNum;
    }
}
