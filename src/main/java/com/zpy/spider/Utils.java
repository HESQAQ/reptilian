package com.zpy.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/8/3.
 */
public class Utils {

    public static String getNum(String  str){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String result =m.replaceAll("").trim();
        return result;
    }
}
