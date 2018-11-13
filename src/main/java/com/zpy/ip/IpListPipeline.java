package com.zpy.ip;

import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.scheduler.SchedulerContext;

import java.util.List;

@PipelineName("IpListPipeline")
public class IpListPipeline implements Pipeline<IpList> {

    @Override
    public void process(IpList ipList) {
        HttpRequest currRequest = ipList.getRequest();
        List list=ipList.getIps();
       // String iop=ipList.getIop();
        //下一页继续抓取
        String currPage = ipList.getCurrPage();
        int nextPage = Integer.parseInt(currPage) + 1;
        if(nextPage <= 100) {
            String nextUrl = "";
            String currUrl = currRequest.getUrl();
            if(currUrl.indexOf("inha") != -1) {
                nextUrl = "https://www.kuaidaili.com/free/inha/"+nextPage+"/";
            } else {
                nextUrl = currUrl + "/" + nextPage +"/";
            }
            SchedulerContext.into(currRequest.subRequest(nextUrl));
        }
    }

}
