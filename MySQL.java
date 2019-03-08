package Server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.ResultSet;

public class MySQL {
	private String path;  //·��
	private String host;  //������
	private String user;  //�û�
	private Connection con;
	private Statement sql;
	private String table;  //������
	private LinkedList<String> tables;         //���б�
	
	/*
	 * ����MySQL����
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
	 * ������������
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
	 * ����MySQL
	 */
	private void linkMySQL() {
		try {
			con = (Connection) DriverManager.getConnection(path, host, user);
			sql = con.createStatement();
			ResultSet rs = sql.executeQuery("show tables");   //��ñ��б�
			tables = new LinkedList<String>();
			while(rs.next()) {
				tables.add(rs.getString(1));
			}
			table = tables.getFirst();       //Ĭ��Ϊ��һ����
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("����ʧ��");
		}
	}
	
	/*
	 * ѡ���
	 */
	public void chooseTable(String table) {  //�������쳣����д����׳�
		if(tables.contains(table)) {
			this.table = table;
		} else {
			System.out.println("�����ڸñ�");
		}
	}
	
	/*
	 * ��ñ����ݿ�ı��б�
	 */
	public LinkedList<String> getTables() {
		return tables;
	}
	
	/*
	 * ���Ҳ���
	 */
	public ResultSet find(String key, String value) {
		ResultSet rs = null;
		String preStr = "select * from " + table + " where " + key + " = ?";
		try {
			PreparedStatement presql = con.prepareStatement(preStr);  //����Ԥ�������
			presql.setString(1, value);
			rs = presql.executeQuery();
		} catch (SQLException e) {
			return null;
		}
		return rs;
	}
	
	/*
	 * ����Ԫ��
	 */
	public void insert(LinkedList<String> elementsList) {
		String sqlStr = "insert into " + table + " values ";
		StringBuffer elements = new StringBuffer("(");     //��������Ԫ��
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
			System.out.println("����ʧ��");
		}
	}
	
	/*
	 * ɾ��Ԫ��
	 */
	public void delete(String mainKey, String mainKeyValue) {
		mainKeyValue = "'" + mainKeyValue + "'";
		String sqlStr = "delete from " + table + " where " + mainKey + " = " + mainKeyValue;
		try {
			sql.executeUpdate(sqlStr);
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("ɾ��ʧ��");
		}
	}
	
	/*
	 * �޸Ĳ���
	 */
	public void modify(String mainKey, String mainKeyValue, String key, String value) {
		mainKeyValue = "'" + mainKeyValue + "'";
		value = "'" + value + "'";
		String sqlStr = "update " + table + " set " + key + " = " + value + " where " + mainKey + " = " + mainKeyValue;
		try {
			sql.executeUpdate(sqlStr);
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("�޸�ʧ��");
		}
	}
	
	/*
	 * �ر�MySQL����
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
