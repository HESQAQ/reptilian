package com.zpy.ip;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.spider.HtmlBean;
import lombok.Data;

@Data
public class IpDetail implements HtmlBean {

    private static final long serialVersionUID = 2555530396237160927L;
   // @Text
    @HtmlField(cssPath="td:nth-child(1)")
    private String ip;
    //@Text
    @HtmlField(cssPath="td:nth-child(2)")
    private String port;

}
