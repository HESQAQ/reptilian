package com.zpy.dao.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by Administrator on 2018/5/29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KpiTable implements java.io.Serializable{

    private Date timeStamp;	//统计时间
    private String tablename;//表名字
    private String fromcode;//数据来源
    private String areaId;//地区编码
    private String areaField;//地区编码的字段名
    private String typeId;//粒度类型id
    private String typeField;//粒度类型在表中的字段名
    private Date endTime;//某一天的结束时间



    public KpiTable(Date timeStamp, String tablename, String fromcode, String areaId,String areaField,String typeId,String typeField) {
        this.timeStamp = timeStamp;
        this.tablename = tablename;
        this.fromcode = fromcode;
        this.areaId = areaId;
        this.areaField=areaField;
        this.typeId=typeId;
        this.typeField=typeField;
    }
}
