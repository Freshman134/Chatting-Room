package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server extends Thread {
	private MySQL database;
	private ServerSocket server;  //服务器监听
	boolean run = true;             //是否持续运行服务器
	HashMap<String, ChildThread> childTreads;   //创建子线程列表 IP : 线程
	private ServerFrame frame;
	
	
	public Server(int port) {
		database = new MySQL("server1", "127.0.0.0", "root");
		childTreads = new HashMap<String, ChildThread>();
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("创建失败");
		}
	}
	
	
	public Server(int port, ServerFrame frame) {
		this.frame = frame;
		database = new MySQL("server1", "127.0.0.0", "root");
		childTreads = new HashMap<String, ChildThread>();
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("创建失败");
		}
	}
	
	
	/*
	 * 测试用
	 */
	public static void main(String[] args) {
		Server server = new Server(8080);
		server.start();
	}
	
	
	/*
	 * 监听线程
	 */
	public void run() {
		while(run) {
			listen();
		}
	}
	
	
	/*
	 * 监听方法
	 */
	private void listen() {
		try {
			System.out.println("正在监听。。。");
			Socket socket = server.accept();
			String IP = socket.getInetAddress().toString();
			ChildThread childThread = new ChildThread(this, socket);  //创建子线程
			childTreads.put(IP, childThread);                                //建立IP : 子线程表
			childThread.start();                                                   //子线程开启
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("连接建立失败");
		}
	}
	
	
	/*
	 * 当子线程要调用MySQL时使用这个方法，生成指令字符串
	 */
	synchronized String operate(int identify, String mess) {
		String str = null;
		switch (identify) {
			case 0x1 : {
				
				String separator = "&&%%@@!!";
				String escapeCharacter = "!!&%@&#@";
				Message m = new Message();
				m.autoSeparator = separator;
				m.autoEscapeCharacter = escapeCharacter;
				
				String [] strs = m.autoAnalysis(mess);
				String acc = strs[0];
				String pwd = strs[1];
				String userIP = strs[2];
				
				String yes = "#11111";
				String no = "#00000";
				String no_1 = "#00001";
				
				database.chooseTable("pwd_table");
				ResultSet rs = database.find(acc, pwd);
				if(rs == null) {
					str = no;
				} else {
					boolean flag = frame.online(acc);
					if(flag) {
						str = no_1;
						break;
					}
					str = yes;
					frame.add(acc);
					database.chooseTable("friendlist");
					database.modify("friend_account", acc, "friendsocket", userIP);
				}
				break;
				
				
			}
			case 0x2 : {
				
				String acc = mess;
				database.chooseTable("friendlist");
				ResultSet rs = database.find("account", acc);
				LinkedList<String> list = new LinkedList<String>();
				
				if(rs == null) {
					str = " ";
					break;
				} else {
					try {
						while(rs.next()) {
							String friend = rs.getString(2);
							String socket = rs.getString(3);
							list.add(friend);
							list.add(socket);
						}
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				
				str = turnListIntoStr(list);
				
				break;
			} case 0x3 : {
				String separator = "@@##";
				String escapeCharacter = "@##@#@@#";
				
				Message m = new Message();
				m.autoSeparator = separator;
				m.autoEscapeCharacter = escapeCharacter;
				
				String [] strs = m.autoAnalysis(mess);
				String new_acc = strs[0];
				String pwd = strs[1];
				String socket = strs[2];
				
				String yes = "#00001";
				String no = "#00000";
				
				database.chooseTable("pwd_table");
				ResultSet rs = database.find("account", new_acc);
				try {
					if(rs.next()) {
						str = no;
					} else {
						str = yes;
						
						LinkedList<String> list = new LinkedList<>();
						list.add(new_acc);
						list.add(pwd);
						database.insert(list);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				break;
			}
		}
		
		return str;
	}

	
	/*
	 * 将链表转换成字符串
	 */
	public String turnListIntoStr(LinkedList<String> list) {
		StringBuffer strbuff = new StringBuffer();
		
		for(String str : list) {
			str = str + " ";
			strbuff.append(str);
		}
		
		String str = strbuff.toString();
		str = str.trim();
		
		return str;
	}
	
}





class ChildThread extends Thread {
	Socket socket;        //用户套接字
	Server server;   //服务器对象
	boolean run = true; //是否持续运行
	DataInputStream in;
	DataOutputStream out;      //输入输出流对象
	
	
	ChildThread(Server server, Socket socket) {
		this.socket = socket;
		this.server = server;
		try {//创建输入输出流对象
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			System.out.println("子线程创建成功");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("子线程创建失败");
		}
	}
	
	
	/*
	 *子线程运行 
	 */
	public void run() {
		while(run) {
			//监听客户端
			try {
				int len = in.available();
				byte [] bytes = new byte [len];
				if(len != 0) {
					in.readFully(bytes);
					//将输入流放入字节数组
					String message = new String(bytes);
					operate(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * 发送给客户端
	 */
	synchronized public void sent(String message) throws IOException {
		out.write(message.getBytes());
	}
	
	
	/*
	 * 对信息进行分析操作，并作出回应
	 */
	private void operate(String message) {
		String [] temps = Message.analysis(message);
		int identify = Integer.parseInt(temps[0]);  //标识
		String source = temps[1];   //源地址
		String destination = temps[2]; //目的地
		String mess = temps[3];     //消息体
		   //根据标识进行方法调用
		
		switch (identify) {
			case 0x1 : {
			} case 0x2 : {
			} case 0x3 : {  //1、登录 2、获得个人信息 3、注册
				String new_mess = server.operate(identify, mess);
				if(new_mess == null) {
					System.out.println("不发送");
					linkClose();
					break;
				}
				new_mess = Message.generate(identify, source, destination, new_mess);
				try {
					sent(new_mess);
					linkClose(); 
				} catch (IOException e) {
					e.printStackTrace();
				} 
				break;
			} 
		}
	}
	
	
	/*
	 * 关闭连接
	 */
	void linkClose() {
		try {
			socket.close();
			socket = null;
			in = null;
			out = null;
			this.stop();
			destory();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * 删除自己
	 */
	private void destory() {
		
		
	}
	
}