package Server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.ResultSet;

public class MySQL {
	private String path;  //路径
	private String host;  //主机名
	private String user;  //用户
	private Connection con;
	private Statement sql;
	private String table;  //表名字
	private LinkedList<String> tables;         //表列表
	
	/*
	 * 创建MySQL连接
	 */
	public MySQL(String DatabaseName, String host, String user) {
		this.path = "jdbc:mysql://localhost:3306/" + DatabaseName + "? useSSL=true";
		this.host = host;
		this.user = user;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			linkMySQL();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 测试用主函数
	 */
	public static void main(String [] args) {
		MySQL mysql = new MySQL("server1", "127.0.0.0", "root");
		mysql.chooseTable("pwd_table");
		LinkedList<String> elementsList = new LinkedList<String>();
		String acc = "account";
		String pwd = "123456";
		elementsList.add(acc);
		elementsList.add(pwd);
		mysql.insert(elementsList);
	}
	
	/*
	 * 连接MySQL
	 */
	private void linkMySQL() {
		try {
			con = (Connection) DriverManager.getConnection(path, host, user);
			sql = con.createStatement();
			ResultSet rs = sql.executeQuery("show tables");   //获得表列表
			tables = new LinkedList<String>();
			while(rs.next()) {
				tables.add(rs.getString(1));
			}
			table = tables.getFirst();       //默认为第一个表
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("连接失败");
		}
	}
	
	/*
	 * 选择表
	 */
	public void chooseTable(String table) {  //可能用异常类进行错误抛出
		if(tables.contains(table)) {
			this.table = table;
		} else {
			System.out.println("不存在该表");
		}
	}
	
	/*
	 * 获得本数据库的表列表
	 */
	public LinkedList<String> getTables() {
		return tables;
	}
	
	/*
	 * 查找操作
	 */
	public ResultSet find(String key, String value) {
		ResultSet rs = null;
		String preStr = "select * from " + table + " where " + key + " = ?";
		try {
			PreparedStatement presql = con.prepareStatement(preStr);  //创建预处理语句
			presql.setString(1, value);
			rs = presql.executeQuery();
		} catch (SQLException e) {
			return null;
		}
		return rs;
	}
	
	/*
	 * 增加元素
	 */
	public void insert(LinkedList<String> elementsList) {
		String sqlStr = "insert into " + table + " values ";
		StringBuffer elements = new StringBuffer("(");     //创建数据元素
		for(String str : elementsList) {
			str = "'" + str + "'";
			elements.append(str + ",");
		}
		elements.deleteCharAt(elements.length() - 1);
		elements.append(")");
		sqlStr = sqlStr + elements.toString();
		try {
			sql.executeUpdate(sqlStr);
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("插入失败");
		}
	}
	
	/*
	 * 删除元素
	 */
	public void delete(String mainKey, String mainKeyValue) {
		mainKeyValue = "'" + mainKeyValue + "'";
		String sqlStr = "delete from " + table + " where " + mainKey + " = " + mainKeyValue;
		try {
			sql.executeUpdate(sqlStr);
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("删除失败");
		}
	}
	
	/*
	 * 修改操作
	 */
	public void modify(String mainKey, String mainKeyValue, String key, String value) {
		mainKeyValue = "'" + mainKeyValue + "'";
		value = "'" + value + "'";
		String sqlStr = "update " + table + " set " + key + " = " + value + " where " + mainKey + " = " + mainKeyValue;
		try {
			sql.executeUpdate(sqlStr);
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("修改失败");
		}
	}
	
	/*
	 * 关闭MySQL连接
	 */
	public void mySQLClosed() {
		try {
			con.close();
			sql = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
