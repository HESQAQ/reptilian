package com.zpy.dao.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2018/7/18.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScenicAll implements java.io.Serializable{
    private String  areaId;//景区id
    private String  areaName;//景区名称
}
