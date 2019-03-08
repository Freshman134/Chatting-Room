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
	private ServerSocket server;  //����������
	boolean run = true;             //�Ƿ�������з�����
	HashMap<String, ChildThread> childTreads;   //�������߳��б� IP : �߳�
	private ServerFrame frame;
	
	
	public Server(int port) {
		database = new MySQL("server1", "127.0.0.0", "root");
		childTreads = new HashMap<String, ChildThread>();
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("����ʧ��");
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
			System.out.println("����ʧ��");
		}
	}
	
	
	/*
	 * ������
	 */
	public static void main(String[] args) {
		Server server = new Server(8080);
		server.start();
	}
	
	
	/*
	 * �����߳�
	 */
	public void run() {
		while(run) {
			listen();
		}
	}
	
	
	/*
	 * ��������
	 */
	private void listen() {
		try {
			System.out.println("���ڼ���������");
			Socket socket = server.accept();
			String IP = socket.getInetAddress().toString();
			ChildThread childThread = new ChildThread(this, socket);  //�������߳�
			childTreads.put(IP, childThread);                                //����IP : ���̱߳�
			childThread.start();                                                   //���߳̿���
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("���ӽ���ʧ��");
		}
	}
	
	
	/*
	 * �����߳�Ҫ����MySQLʱʹ���������������ָ���ַ���
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
						// TODO �Զ����ɵ� catch ��
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
	 * ������ת�����ַ���
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
	Socket socket;        //�û��׽���
	Server server;   //����������
	boolean run = true; //�Ƿ��������
	DataInputStream in;
	DataOutputStream out;      //�������������
	
	
	ChildThread(Server server, Socket socket) {
		this.socket = socket;
		this.server = server;
		try {//�����������������
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			System.out.println("���̴߳����ɹ�");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("���̴߳���ʧ��");
		}
	}
	
	
	/*
	 *���߳����� 
	 */
	public void run() {
		while(run) {
			//�����ͻ���
			try {
				int len = in.available();
				byte [] bytes = new byte [len];
				if(len != 0) {
					in.readFully(bytes);
					//�������������ֽ�����
					String message = new String(bytes);
					operate(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * ���͸��ͻ���
	 */
	synchronized public void sent(String message) throws IOException {
		out.write(message.getBytes());
	}
	
	
	/*
	 * ����Ϣ���з�����������������Ӧ
	 */
	private void operate(String message) {
		String [] temps = Message.analysis(message);
		int identify = Integer.parseInt(temps[0]);  //��ʶ
		String source = temps[1];   //Դ��ַ
		String destination = temps[2]; //Ŀ�ĵ�
		String mess = temps[3];     //��Ϣ��
		   //���ݱ�ʶ���з�������
		
		switch (identify) {
			case 0x1 : {
			} case 0x2 : {
			} case 0x3 : {  //1����¼ 2����ø�����Ϣ 3��ע��
				String new_mess = server.operate(identify, mess);
				if(new_mess == null) {
					System.out.println("������");
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
	 * �ر�����
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
	 * ɾ���Լ�
	 */
	private void destory() {
		
		
	}
	
}