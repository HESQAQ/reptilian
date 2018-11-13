package com.zpy.dao.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2018/7/19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Color implements java.io.Serializable{
    private String id;
    private String name;
    private String des;
    private String tt;

}
