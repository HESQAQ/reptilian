package com.zpy.gecco;

import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;

/**
 * Created by Administrator on 2018/8/1.
 */

@PipelineName("aaaa")
public class Demo implements Pipeline<MyGithub> {
    @Override
    public void process(MyGithub aa) {

        System.out.println("--"+aa);


    }
}
