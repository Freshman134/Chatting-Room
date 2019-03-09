package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import Server.Message;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Client extends Thread {
	UDPClient udpClient;       //udpclient
	Socket mySocket;          //用户套接字
	boolean run;     //是否持续运行
	DataInputStream in;
	DataOutputStream out;  //创建连接的输入输出流对象
//	FileOutputStream out;   //输出到主机的信息
	LoginFrame loginFrame;   //登陆界面对象
	UserFrame userFrame;       //用户界面对象
	private static final String SERVERIP = "127.0.0.0/20";
	int port;
	String account;
	String passward;
	
	
	public Client(int port) {
//		mkdir();
		this.port = port;
		run = true;
	}
	
	
	/*
	 * 连接服务器
	 */
	void linkServer() {
		mySocket = new Socket();
		try {
			InetAddress serverIP = InetAddress.getLocalHost();  //服务器IP,暂时设为本地
			InetSocketAddress inetSocketAddress = new InetSocketAddress(serverIP, port);  //创建服务器连接套接字
			mySocket.connect(inetSocketAddress);	
			out = new DataOutputStream(mySocket.getOutputStream());
			in = new DataInputStream(mySocket.getInputStream());                  //建立输入输出流对象
			if(run) {
				this.start();
			} else {
				run = true;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	/*
	 * 测试用
	 */
	public static void main(String[] args) {

	}
	
	
	/*
	 * 关闭连接
	 */
	void linkClose() {
		try {
			mySocket.close();
			mySocket = null;
			in = null;
			out = null;
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * 创建客户端存储目录
	 */
	private void mkdir() {
//		File f0 = new File("ClientData");
//		if(!f0.exists())
//			f0.mkdirs();
//		f0 = new File("ClientData//SystemLog");
//		if(!f0.exists())
//			f0.mkdirs();
//		f0 = new File("ClientData//FriendsData");
//		if(!f0.exists())
//			f0.mkdirs();
	}

	
	/*
	 * 发送给服务器，只提供发送功能
	 */
	public void sent(String message) throws IOException {
		out.write(message.getBytes());
	}
	
	
	/*
	 * 读取线程的操作方法
	 */
	private void operate(String command) {
		String [] temps = Message.analysis(command);
		int identify = Integer.parseInt(temps[0]);  //标识
		String source = temps[1];   //源地址
		String destination = temps[2]; //目的地
		String message = temps[3];     //消息体
		   //根据标识进行方法调用
		
		switch (identify) {
			case 0x1 : {
				
				String no_1 = "#00001";
				String yes = "#11111";
				String no = "#00000";
				if(message.equals(yes)) {
					loginFrame.setVisible(false);
					linkClose();
					@SuppressWarnings("unused")
					UserFrame userFrame = new UserFrame(this);
				} else if (message.equals(no)) {
					account = "";
					passward = "";
					JOptionPane.showMessageDialog(null, "密码或账号输入有误！！", "错误", JOptionPane.ERROR_MESSAGE);
					linkClose();
				} else if (message.equals(no_1)) {
					account = "";
					passward = "";
					JOptionPane.showMessageDialog(null, "该用户已被登录", "警告", JOptionPane.WARNING_MESSAGE);
					linkClose();
				} else {
					System.out.println("有错误！！");
					linkClose();
				}
				
				
				break;
			} 
			case 0x2 : {  //收到个人信息
				
				
				if(message.equals(" ")) {
					//没有好友
				} else {
					String [] strs = message.split(" ");
					userFrame.friendSocketList = new HashMap<String, String>();
					for(int i = 0; i < strs.length; i = i + 2) {
						System.out.print(strs[i] + "：");
						System.out.println(strs[i + 1]);
						String acc = strs[i];
						String soc = strs[i + 1];
						userFrame.friendSocketList.put(acc, soc);
						userFrame.setVisible(true);
					}
					userFrame.componmentInitalize();
				}
				linkClose();
				
				
				break;
			} case 0x3 : {//收到注册回复
				String yes = "#00001";
				String no = "#00000";
				
				if(message.equals(yes)) {
					JOptionPane.showMessageDialog(null, "注册成功！请退出注册界面。", "提示", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "注册失败！", "警告", JOptionPane.ERROR_MESSAGE);
				}
				
				break;
			}
		}
		
	}
	
	
	/*
	 * 读取线程运行
	 */
	public void run() {
		while(true) {
			while(run) {
				try {
					int len = in.available();
					byte [] bytes = new byte [len];
					if(len != 0) {
						in.readFully(bytes);							//将输入流放入字节数组
						String message = new String(bytes);
						operate(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * 登录
	 */
	public void login(String acc, String pwd, LoginFrame tempFrame) {
		linkServer();
		this.loginFrame = tempFrame;
		
		String sparator = "&&%%@@!!";
		String escapeCharacter = "!!&%@&#@";
		Message m = new Message();
		m.autoSeparator = sparator;
		m.autoEscapeCharacter = escapeCharacter;
		String ownIP = mySocket.getInetAddress().getHostAddress();
		String message = m.autoGenerate(acc, pwd, ownIP);
		account = acc;
		passward = pwd;
		
		int identify = 0x1;
		String mess = Message.generate(identify, acc, SERVERIP, message);
		try {  //发送
			sent(mess);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * 获取个人信息
	 */
	public void getPersonalData(UserFrame tempFrame) {
		this.userFrame = tempFrame;
		
		String mess = Message.generate(0x2, account, SERVERIP, account);
		try {
			linkServer();
			sent(mess);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * 注册
	 */
	public void register(String acc, String pwd) {
		linkServer();
		Message message = new Message();
		message.autoSeparator = "@@##";
		message.autoEscapeCharacter = "@##@#@@#";
		String ownIP = mySocket.getInetAddress().getHostAddress();
		String mess = message.autoGenerate(acc, pwd, ownIP);
		
		int identify = 0x3;
		
		String packetMessage = Message.generate(identify, ownIP, SERVERIP, mess);
		try {
			sent(packetMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * 停止监听
	 */
	private void close() {
		run = false;
	}
	
}
