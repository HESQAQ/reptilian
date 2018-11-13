package com.zpy.gecco;

import com.geccocrawler.gecco.annotation.Html;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Image;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.spider.HtmlBean;
import lombok.Data;

/**
 * Created by Administrator on 2018/7/31.
 */
@Data
public class StarDetail implements HtmlBean {

    //明星的照片
   /* @Image("src")
    @HtmlField(cssPath = "a > img")
    private  String PhotoString;*/
    //明星的名字
    @Html
    @HtmlField(cssPath ="div > div > h4")
    private String  starNameHtml;

    //明星的性别
    @Text
    @HtmlField(cssPath = "div > p:nth-child(2)")
    private  String starSex;

    //明星的职业
    @Html
    @HtmlField(cssPath = "div > p:nth-child(3)")
    private String professionHtml;

    //明星的国籍
    @Text
    @HtmlField(cssPath = "div > p:nth-child(4)")
    private String  nationality;
    //明星的出生日期
    @Text
    @HtmlField(cssPath = "div > p.special")
    private String birthday;
    //明星的星座
    @Text
    @HtmlField(cssPath = "div > p:nth-child(6)>a")
    private String constellation;

    //明星的身高
    @Text
    @HtmlField(cssPath = "div > p:nth-child(7)")
    private String height;


}
