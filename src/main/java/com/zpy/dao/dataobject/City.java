package com.zpy.dao.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2018/8/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City  implements java.io.Serializable{
    private String cid;
    private String cname;
    private String head;
    private String pinyin;
}
