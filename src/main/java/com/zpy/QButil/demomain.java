package com.zpy.QButil;

import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;

/**
 * Created by Administrator on 2018/8/2.
 */
public class demomain {
    public static void main(String[] args){
        QueryRunner runner = DBQuerier.getInstance().getQueryRunner();
        Object[] os1=new Object[]{"001","blue","蓝色","BLUE"};
        Object[] os2=new Object[]{"002","white","白色","WHITE"};
        Object[][] oss=new Object[2][];
        oss[0]=os1;
        oss[1]=os2;
        String sql="insert into color(id,name,des,tt) values(?,?,?,?)";
        String sql2="select * from color";
        try {
            //queryRunner.update(sql,os1);
            runner.batch(sql,oss);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
