package com.zpy.dao.dataobject;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/8/23.
 */
@Data
public class Detail {
    private String id;
    private String uid;
    private String title;
    private String content;
    private String date;
    private BigDecimal score;
    private String isselect;
    private List<String>  simgs;
    private List<String> bimgs;
    private List<String>  videos;
    private String replyeditor;
    private String reply;
    private String usecount;
    private String sourcetype;
    private String playdate;
    private String keywordlist;
    private String cmttype;
}
