package com.zpy.base.createsource;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>ClassName:GetSqlFileds</p>
 * <p>Description:取得数据库表相应的信息</p>
 * @author wangxiaobo
 * @date 2015-11-11
 */
public class GetSqlFileds {
	
	/**
	 *Oracle数据库链接用于得到带注解的实体类
	 */
	public List<String> getSqlWithFiledsCom(String url,String user,String password,String tableName)
	{
	    Connection con = null;// 创建一个数据库连接
	    PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet result = null;// 创建一个结果集对象
	    String strFileds = null;
	    List<String> filedsList = new ArrayList<String>();
	    try
	    {
	        Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
	        System.out.println("开始尝试连接数据库！");
	        con = DriverManager.getConnection(url, user, password);// 获取连接
	        System.out.println("连接成功！");
	        StringBuffer sb = new StringBuffer();
	        sb.append("select 'private ' || (case");
	        sb.append(" when instr(c.DATA_TYPE, 'VARCHAR2') > 0 then");
	        sb.append(" 'String'");
	        sb.append(" when instr(c.DATA_TYPE, 'CHAR') > 0 then");
	        sb.append(" 'String'");
	        sb.append(" when instr(c.DATA_TYPE, 'DATE') > 0 then");
	        sb.append(" 'Date'");
	        sb.append("when instr(c.DATA_TYPE, 'NUMBER') > 0 then");
	        sb.append(" 'Long'");
	        sb.append(" end) || ' ' || lower(c.COLUMN_NAME) || ';	//' || o.comments");
	        sb.append("  from user_tab_columns c, USER_COL_COMMENTS o");
	        sb.append(" where c.TABLE_NAME = o.table_name and c.COLUMN_NAME = o.column_name and c.TABLE_NAME = upper('"+tableName+"')");
	        sb.append(" order by c.COLUMN_ID");
	        System.out.println(sb.toString());
	        pre = con.prepareStatement(sb.toString());// 实例化预编译语句
	        result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
	        while (result.next()){ // 当结果集不为空时
	        	strFileds  = result.getString(1);
	        	filedsList.add(strFileds);
	        }
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	        try
	        {
	            // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
	            // 注意关闭的顺序，最后使用的最先关闭
	            if (result != null)
	                result.close();
	            if (pre != null)
	                pre.close();
	            if (con != null)
	                con.close();
	            System.out.println("数据库连接已关闭！");
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	    }
	    System.out.println(filedsList);
		return filedsList;
	}
	
	/**
	 *Oracle数据库链接
	 */
	public String getSqlWithFileds(String url,String user,String password,String tableName)
	{
	    Connection con = null;// 创建一个数据库连接
	    PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet result = null;// 创建一个结果集对象
	    String strFileds = "";
	    try
	    {
	        Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
	        System.out.println("开始尝试连接数据库！");
	        con = DriverManager.getConnection(url, user, password);// 获取连接
	        System.out.println("连接成功！");
	        StringBuffer sb = new StringBuffer();
	        sb.append("select lower(column_name)");
	        sb.append("||':'|| (case");
	        sb.append(" when instr(u.DATA_TYPE, 'VARCHAR2') > 0 then");
	        sb.append(" 'string'");
	        sb.append(" when instr(u.DATA_TYPE, 'CHAR') > 0 then");
	        sb.append(" 'string'");
	        sb.append(" when instr(u.DATA_TYPE, 'DATE') > 0 then");
	        sb.append(" 'date'");
	        sb.append("when instr(u.DATA_TYPE, 'NUMBER') > 0 then");
	        sb.append(" 'long'");
	        sb.append(" end) column_name");
	        sb.append("  FROM user_tab_columns u  ");
	        sb.append("  WHERE table_name = upper('" + tableName + "')  ");
	        sb.append("  order by column_id ");
	        sb.append("");
	        sb.append("");
	        System.out.println(sb.toString());
	        pre = con.prepareStatement(sb.toString());// 实例化预编译语句
	      
	        result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
	        while (result.next()){ // 当结果集不为空时
	        	strFileds  = strFileds + result.getString(1)+",";
	        }
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	        try
	        {
	            // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
	            // 注意关闭的顺序，最后使用的最先关闭
	            if (result != null)
	                result.close();
	            if (pre != null)
	                pre.close();
	            if (con != null)
	                con.close();
	            System.out.println("数据库连接已关闭！");
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	    }
	    if(strFileds!=null&&!"".equals(strFileds)&&strFileds.length()>1){
	    	strFileds = strFileds.substring(0,strFileds.length()-1);
	    }
	    System.out.println(strFileds);
		return strFileds;
	}
	public static void main(String[] args){
		GetSqlFileds getSqlFileds = new GetSqlFileds();
		//生成实体xml配置文件
		//id,role_name,description,status,user_id,creator,gmt_create,modifier,gmt_modify,is_deleted,gmt_deleted
		String sqlFileds = getSqlFileds.getSqlWithFileds("dbc:oracle:thin:@192.168.110.143:1521:orcl","witskydtrk","witskydtrk","sys_menu");
		System.out.println(sqlFileds);
		
	}
}
