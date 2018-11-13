package com.zpy.base.createsource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * 
 * <p>ClassName:CreateSource</p>
 * <p>Description:生成代码神器
 * 开发文件生成工具
 * 使用方法：文件夹自己建 为了防止覆盖已有的文件,如果文件已存在程序终止该程序。</p>
 * 该模块数据库连接在类里面配。完全独立于系统工程
 * @author wangxiaobo
 * @date 2015-11-11
 */
public class CreateSource {
	//表名(必须小写)   如果大写出现问题后果自找
	private static String tableName = "T_DATAMONITOR_ALARM".toLowerCase();

	private static String url="jdbc:oracle:thin:@192.168.150.117:1521/orcl";
	private static String user="tygc";
	private static String password="czty_tygc";
    /////////////////////////////////////////////////////
	
	//方法
	private static String constant_insert = "insert";
	private static String constant_delete = "delete";
	private static String constant_update = "update";
	private static String constant_getById = "getById";
	private static String constant_findAll= "findAll";
	//生成文件存放的位置，该文件夹要选建好后才能进行。
	private static String objectPath = "main.java.cn.witsky.tygc.base.createsource.temp";

	public static void main(String[] args) throws Exception {
		//Oracle数据库链接拿到相关字段信息
		GetSqlFileds getSqlFileds = new GetSqlFileds();
		//生成实体xml配置文件
		String sqlFileds = getSqlFileds.getSqlWithFileds(url,user,password,tableName);
		generateXml(tableName,sqlFileds);
		//生成实体类
		List<String> sqlFiledsComList = getSqlFileds.getSqlWithFiledsCom(url,user,password,tableName);
		generateObj(tableName,sqlFiledsComList);
		//生成dao文件
		generateDao();
		//生成service文件
		generateService();
		//生成Controller文件
		generateController();
		//生成配置文件
		//generateConfig();
	}
	
	/**
	 * 生成配置文件
	 * @throws IOException
	 */
	public static void generateConfig() throws IOException {
		//实体xml配置文件路径为
		String fileName = getFilePath()+ "\\" +getObjectName() + "Config.xml";
		System.out.println("配置文件路径为：" + fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			if (file.createNewFile()) {
				System.out.println("配置文件创建成功！");
			} else {
				System.out.println("配置文件创建失败！");
				return;
			}
		} else {
			System.out.println("程序终止，配置文件已经存在");
			return;
		}
		FileOutputStream out = new FileOutputStream(file, true);
		StringBuffer sb = new StringBuffer();
		sb.append("<!-- sqlmap.xml -->");
		sb.append("\n");
		sb.append("  <sqlMap resource=\""+objectPath.replace(".", "/")+"/entity/"+getObjectName()+".xml\" />");
		sb.append("\n");
		sb.append("\n");
		sb.append("<!-- applicationContext.xml -->");
		sb.append("\n");
		sb.append("  <bean id=\""+toBartoUpperCase(tableName)+"Dao\" class=\""+objectPath+".dao."+getObjectName()+"Dao\"/>");
		out.write(sb.toString().getBytes("utf-8"));
		out.close();
		System.out.println("配置文件生成结束！");
	}
	
	/**
	 * 生成Controller文件
	 * @throws IOException
	 */
	public static void generateController() throws IOException {
		//实体xml配置文件路径为
		String fileName = getFilePath()+ "\\" +getObjectName() + "Controller.java";
		System.out.println("Controller文件路径为：" + fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			if (file.createNewFile()) {
				System.out.println("Controller文件创建成功！");
			} else {
				System.out.println("Controller文件创建失败！");
				return;
			}
		} else {
			System.out.println("程序终止，Controller文件已经存在");
			return;
		}
		FileOutputStream out = new FileOutputStream(file, true);
		StringBuffer sb = new StringBuffer();
		sb.append("package "+objectPath+".controller;");
		sb.append("\n");
		sb.append("\n");
		sb.append("@Controller");
		sb.append("\n");
		sb.append("@RequestMapping(\"不要这么懒自己改一下吧/"+toLowerCaseFirstOne(getObjectName())+"/\")");
		sb.append("\n");
		sb.append("public class "+getObjectName()+"Controller extends BaseController{");
		sb.append("\n");
		sb.append("	@Autowired");
		sb.append("\n");
		sb.append("	private "+getObjectName()+"Service "+toLowerCaseFirstOne(getObjectName())+"Service;");
		sb.append("\n");
		sb.append("\n");
		sb.append("}");
		out.write(sb.toString().getBytes("utf-8"));
		out.close();
		System.out.println("Dao文件生成结束！");
	}
	
	
	/**
	 * 生成Service文件
	 * @throws IOException
	 */
	public static void generateService() throws IOException {
		//实体xml配置文件路径为
		String fileName = getFilePath()+ "\\" +getObjectName() + "Service.java";
		System.out.println("Service文件路径为：" + fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			if (file.createNewFile()) {
				System.out.println("Service文件创建成功！");
			} else {
				System.out.println("Service文件创建失败！");
				return;
			}
		} else {
			System.out.println("程序终止，Service文件已经存在");
			return;
		}
		FileOutputStream out = new FileOutputStream(file, true);
		StringBuffer sb = new StringBuffer();
		sb.append("package "+objectPath+".service;");
		sb.append("\n");
		sb.append("\n");
		sb.append("@Service");
		sb.append("\n");
		sb.append("public class "+getObjectName()+"Service extends BaseService<"+getObjectName()+">{");
		sb.append("\n");
		sb.append("\n");
		sb.append("}");
		out.write(sb.toString().getBytes("utf-8"));
		out.close();
		System.out.println("Dao文件生成结束！");
	}
	
	/**
	 * 生成Dao文件
	 * @throws IOException
	 */
	public static void generateDao() throws IOException {
		//实体xml配置文件路径为
		String fileName = getFilePath()+ "\\" +getObjectName() + "Dao.java";
		System.out.println("Dao文件路径为：" + fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			if (file.createNewFile()) {
				System.out.println("Dao文件创建成功！");
			} else {
				System.out.println("Dao文件创建失败！");
				return;
			}
		} else {
			System.out.println("程序终止，Dao文件已经存在");
			return;
		}
		FileOutputStream out = new FileOutputStream(file, true);
		StringBuffer sb = new StringBuffer();
		sb.append("package "+objectPath+".dao;");
		sb.append("\n");
		sb.append("\n");
		sb.append("@Repository");
		sb.append("\n");
		sb.append("public class "+getObjectName()+"Dao extends BaseDao<"+getObjectName()+">{");
		sb.append("\n");
		sb.append("\n");
		sb.append("}");
		out.write(sb.toString().getBytes("utf-8"));
		out.close();
		System.out.println("Dao文件生成结束！");
	}

	/**
	 * 生成实体对象文件
	 * @throws IOException
	 */
	public static void generateObj(String tableName,List<String> sqlFiledsComList) throws IOException {
		//实体xml配置文件路径为
		String fileName = getFilePath()+ "\\" +getObjectName() + ".java";
		System.out.println("实体对象文件路径为：" + fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			if (file.createNewFile()) {
				System.out.println("实体对象文件创建成功！");
			} else {
				System.out.println("实体对象文件创建失败！");
				return;
			}
		} else {
			System.out.println("程序终止，实体对象已经存在");
			return;
		}
		FileOutputStream out = new FileOutputStream(file, true);
		StringBuffer sb = new StringBuffer();
		sb.append("package "+objectPath+".entity;");
		sb.append("\n");
		sb.append("\n");
		sb.append("public class "+getObjectName()+"  extends BaseEntity implements java.io.Serializable {");
		sb.append("\n");  
		for(String sqlFiledsCom : sqlFiledsComList){
			sb.append("	"+toBartoUpperCase(sqlFiledsCom));  
			sb.append("\n"); 
		}
		sb.append("}"); 
		out.write(sb.toString().getBytes("utf-8"));
		out.close();
		System.out.println("实体对象文件生成结束！");
	}
	
	/**
	 * 生成xml文件
	 * @throws IOException
	 */
	public static void generateXml(String tableName,String sqlFileds) throws IOException {
		//实体xml配置文件路径为
		String fileName = getFilePath()+ "\\" +getObjectName() + ".xml";
		System.out.println("xml配置文件路径为：" + fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			if (file.createNewFile()) {
				System.out.println("xml配置文件创建成功！");
			} else {
				System.out.println("xml配置文件创建失败！");
				return;
			}
		} else {
			System.out.println("程序终止，配置文件已经存在");
			return;
		}
		FileOutputStream out = new FileOutputStream(file, true);
		StringBuffer sb = new StringBuffer();

		//所有字段
		String[] sqlFiledsArray = sqlFileds.split(",");
		//所有字段
		String[] filedsArray = new String[sqlFiledsArray.length];
		//所有字段类型
		String[] filedsTypeArray = new String[sqlFiledsArray.length];
		for (int i = 0; i < sqlFiledsArray.length; i++) {
			String[] temp = sqlFiledsArray[i].split(":");
			if(temp.length<=1){
				filedsArray[i]=temp[0];
				filedsTypeArray[i]=null;
			}else{
				filedsArray[i]=temp[0];
				filedsTypeArray[i]=temp[1];
			}
		}
		//insert语句
		@SuppressWarnings("unused")
		String sqlFiledsBlank = sqlFileds.replace(",", ", ");
		//values语句
		String sqlValues = "#{" + sqlFileds.replace(",", "}, #{") + "}";
		sqlValues = toBartoUpperCase(sqlValues);
		//select所有字段
		String sqlSelectAll = "";
		for (int i = 0; i < filedsArray.length; i++) {
			sqlSelectAll += "t1."+filedsArray[i] +" as " + toBartoUpperCase(filedsArray[i]);
			if (i != filedsArray.length - 1) {
				sqlSelectAll += ", ";
			}
		}
		
		//定义ibats内容
		//声明
		String objectName = getObjectName();
		//定义xml头部
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("\n");
		//sb.append("<!DOCTYPE sqlMap PUBLIC \"-//iBATIS.com//DTD SQL Map 2.0//EN\" \"http://www.ibatis.com/dtd/sql-map-2.dtd\">");
		//<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
		sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		sb.append("\n"); 
		sb.append("<mapper namespace=\"" + objectName + "\">");
		sb.append("\n");
//		sb.append("    <typeAlias alias=\"" + objectName + "\" type=\"" + objectPath + "\" />");
//		sb.append("\n");
		sb.append("\n");  

		//select_all
		sb.append("    <sql id=\""+objectName+"_select_all\">");
		sb.append("\n");
		sb.append("    "+sqlSelectAll);
		sb.append("\n");
		sb.append("    </sql>");
		sb.append("\n");
		sb.append("\n");
		
		//insert
		sb.append("    <insert id=\"" + constant_insert + "\" parameterType=\"" + objectName + "\">");
		sb.append("\n");
    	sb.append("		<selectKey resultType=\"Long\" keyProperty=\"id\" order=\"BEFORE\">");
    	sb.append("\n");
    	sb.append("			SELECT SQ_"+tableName.toUpperCase()+".NEXTVAL  as id FROM DUAL");
    	sb.append("\n");
    	sb.append("		</selectKey>");
    	 
		sb.append("\n");
		//sb.append("		insert into " + tableName + "(" + sqlFiledsBlank + ") ");
		sb.append("		insert into " + tableName + "(");
		for (int i = 0; i < filedsArray.length; i++) {
			sb.append(filedsArray[i]);
			if(i != filedsArray.length-1){
				sb.append(",");
			}
		}
		sb.append(") ");
		
		sb.append("\n");
		//sb.append("		values(" + sqlValues + ")");
		//values(#{id,jdbcType=INTEGER}, #{userName,jdbcType=VARCHAR}, #{parentId,jdbcType=INTEGER}, #{orgId,jdbcType=INTEGER}, #{deptId,jdbcType=INTEGER}, #{duty,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, #{mobile,jdbcType=VARCHAR}, #{zipCode,jdbcType=VARCHAR}, #{sex,jdbcType=VARCHAR}, #{birthday,jdbcType=DATE}, #{idCard,jdbcType=VARCHAR}, #{post,jdbcType=VARCHAR}, #{creator,jdbcType=VARCHAR}, #{gmtCreate,jdbcType=DATE}, #{modifier,jdbcType=VARCHAR}, #{gmtModify,jdbcType=DATE}, #{isDeleted,jdbcType=VARCHAR}, #{gmtDeleted,jdbcType=DATE})
		sb.append("		values(");
		for (int i = 0; i < filedsArray.length; i++) {
			if(filedsTypeArray[i]!=null){
				sb.append("#{"+toBartoUpperCase(filedsArray[i])+",jdbcType="+getJdbcType(filedsTypeArray[i])+"}");
			}else{
				sb.append("#{"+toBartoUpperCase(filedsArray[i])+"}");
			}
			if(i != filedsArray.length-1){
				sb.append(",");
			}
			
		}
		sb.append(")");
		
		sb.append("\n");
		sb.append("    </insert>");
		sb.append("\n");
		sb.append("\n");
		
		//update
		String sqlUpdate = "";
		for (int i = 0; i < filedsArray.length; i++) {
			if(!"id".equals(filedsArray[i])){
				sqlUpdate += "			<if test=\"null != "+toBartoUpperCase(filedsArray[i])+"\">t1."+filedsArray[i] +" = #{" + toBartoUpperCase(filedsArray[i]) + "}";
				sqlUpdate += ",";
				sqlUpdate+= "</if>";
				sqlUpdate+= "\n";
			}
			
		}
		sb.append("    <update id=\"" + constant_update + "\" parameterType=\"" + objectName + "\">");
		sb.append("\n");
		sb.append("		update " + tableName + " t1 set \n" + sqlUpdate + "			<if test=\"null != id\">t1.id = #{id}</if>");
		sb.append("\n");
		sb.append("       where t1.id = #{id}");
		sb.append("\n");
		sb.append("    </update>");
		sb.append("\n");
		sb.append("\n");
		
		//delete物理删除
		sb.append("    <delete id=\"" + constant_delete + "\" parameterType=\""+objectName+"\">");
		sb.append("\n");
		sb.append("		delete from  " + tableName + " t1 where 	t1.id=#{id}");
		sb.append("\n");
		sb.append("    </delete>");
		sb.append("\n");
		sb.append("\n");
		
		//getById
		sb.append("    <select id=\"" + constant_getById + "\" parameterType=\"String\"" + " resultType=\"" + objectName + "\">");
		sb.append("\n");
		sb.append("        select");
		sb.append("\n");
		sb.append("        <include refid=\""+objectName+"_select_all\" /> ");
		sb.append("\n");
		sb.append("        from "+tableName+" t1 where t1.id=#{id}");
		sb.append("\n");
		sb.append("    </select>");
		sb.append("\n");
		sb.append("\n");
		
		//select
		sb.append("    <select id=\"" + constant_findAll + "\" parameterType=\""+objectName+"\"" + " resultType=\"" + objectName + "\">");
		sb.append("\n");
		sb.append("        select");
		sb.append("\n");
		sb.append("        <include refid=\""+objectName+"_select_all\" /> ");
		sb.append("\n");
		sb.append("        from "+tableName+" t1 where 1=1");
		sb.append("\n");
		//循环列出条件
		for (int i = 0; i < filedsArray.length; i++) {
			//sb.append("			<isNotEmpty prepend=\"AND\" property=\"" + toBartoUpperCase(filedsArray[i]) + "\">");
			sb.append("		<if test=\""+toBartoUpperCase(filedsArray[i])+" != null and "+toBartoUpperCase(filedsArray[i])+" != ''\">");
			//sb.append("(" + filedsArray[i] + "=#{" + toBartoUpperCase(filedsArray[i]) + "})");
			sb.append(" and t1."+filedsArray[i]+" = #{"+toBartoUpperCase(filedsArray[i])+"} ");
			sb.append("</if>");
			sb.append("\n");
		}
		sb.append("	</select>");
		sb.append("\n");
		sb.append("\n");
		
		sb.append("</mapper>");
		out.write(sb.toString().getBytes("utf-8"));
		out.close();
		System.out.println("XML配置文件生成结束！");
	}


	/**
	 * 获取对象名
	 * @return
	 */
	public static String getObjectName() {
		return toBartoUpperCase(toUpperCaseFirstOne(delFirstT(tableName)));
	}
	
	/**
	 * 取得ibats配置文件的目录
	 * @return
	 */
	public static String getFilePath() {
		//根目录
		String dir = System.getProperty("user.dir");
		//模块包目录
		dir = dir + "\\" + "mybatis" + "\\";
		return dir;
	}

	/**
	 * 首字母转小写
	 * 
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(
					Character.toLowerCase(s.charAt(0))).append(s.substring(1))
					.toString();
	}
	
	/**
	 * 首字母转大写
	 * 
	 * @param s
	 * @return
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(
					Character.toUpperCase(s.charAt(0))).append(s.substring(1))
					.toString();
	}
	
	/**
	 * 带"_"线的下一个英文字符变成大写
	 * @param s
	 * @return
	 */
	public static String toBartoUpperCase(String s){
		boolean check = false;
		char v2[] = s.toCharArray();
		String result = "";
		for(char v: v2){
			String strTemp = String.valueOf(v);
			if(check){
				strTemp = strTemp.toUpperCase();
			}
			if(strTemp.equals("_")){
				check=true;
			}else{
				check=false;
				result = result+strTemp;
			}
		}
		return result;
	}
	/**
	 * 通过字段类型返回Mybatis的jdbcType
	 * @param type 字段类型
	 * @return
	 */
	public static String getJdbcType(String type){
		if(type!=null){
			if("long".equals(type)){
				return "INTEGER";
			}else if("string".equals(type)){
				return "VARCHAR";
			}else if("date".equals(type)){
				return "DATE";
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	/**
	 * 带"t_"开头的表把"t_"去掉
	 * @param s
	 * @return
	 */
	public static String delFirstT(String s){
		String result = null;
		if(s.substring(0,2).equals("t_")){
			result = s.substring(2, s.length());
		}else{
			result = s;
		}
		return result;
	}
}
