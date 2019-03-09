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
	Socket mySocket;          //�û��׽���
	boolean run;     //�Ƿ��������
	DataInputStream in;
	DataOutputStream out;  //�������ӵ��������������
//	FileOutputStream out;   //�������������Ϣ
	LoginFrame loginFrame;   //��½�������
	UserFrame userFrame;       //�û��������
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
	 * ���ӷ�����
	 */
	void linkServer() {
		mySocket = new Socket();
		try {
			InetAddress serverIP = InetAddress.getLocalHost();  //������IP,��ʱ��Ϊ����
			InetSocketAddress inetSocketAddress = new InetSocketAddress(serverIP, port);  //���������������׽���
			mySocket.connect(inetSocketAddress);	
			out = new DataOutputStream(mySocket.getOutputStream());
			in = new DataInputStream(mySocket.getInputStream());                  //�����������������
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
	 * ������
	 */
	public static void main(String[] args) {

	}
	
	
	/*
	 * �ر�����
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
	 * �����ͻ��˴洢Ŀ¼
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
	 * ���͸���������ֻ�ṩ���͹���
	 */
	public void sent(String message) throws IOException {
		out.write(message.getBytes());
	}
	
	
	/*
	 * ��ȡ�̵߳Ĳ�������
	 */
	private void operate(String command) {
		String [] temps = Message.analysis(command);
		int identify = Integer.parseInt(temps[0]);  //��ʶ
		String source = temps[1];   //Դ��ַ
		String destination = temps[2]; //Ŀ�ĵ�
		String message = temps[3];     //��Ϣ��
		   //���ݱ�ʶ���з�������
		
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
					JOptionPane.showMessageDialog(null, "������˺��������󣡣�", "����", JOptionPane.ERROR_MESSAGE);
					linkClose();
				} else if (message.equals(no_1)) {
					account = "";
					passward = "";
					JOptionPane.showMessageDialog(null, "���û��ѱ���¼", "����", JOptionPane.WARNING_MESSAGE);
					linkClose();
				} else {
					System.out.println("�д��󣡣�");
					linkClose();
				}
				
				
				break;
			} 
			case 0x2 : {  //�յ�������Ϣ
				
				
				if(message.equals(" ")) {
					//û�к���
				} else {
					String [] strs = message.split(" ");
					userFrame.friendSocketList = new HashMap<String, String>();
					for(int i = 0; i < strs.length; i = i + 2) {
						System.out.print(strs[i] + "��");
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
			} case 0x3 : {//�յ�ע��ظ�
				String yes = "#00001";
				String no = "#00000";
				
				if(message.equals(yes)) {
					JOptionPane.showMessageDialog(null, "ע��ɹ������˳�ע����档", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "ע��ʧ�ܣ�", "����", JOptionPane.ERROR_MESSAGE);
				}
				
				break;
			}
		}
		
	}
	
	
	/*
	 * ��ȡ�߳�����
	 */
	public void run() {
		while(true) {
			while(run) {
				try {
					int len = in.available();
					byte [] bytes = new byte [len];
					if(len != 0) {
						in.readFully(bytes);							//�������������ֽ�����
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
	 * ��¼
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
		try {  //����
			sent(mess);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * ��ȡ������Ϣ
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
	 * ע��
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
	 * ֹͣ����
	 */
	private void close() {
		run = false;
	}
	
}
