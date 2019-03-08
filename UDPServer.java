package Server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.DatagramPacket;

public class UDPServer extends Thread {
	DatagramSocket ds;
	private Server server;
	private int port;
	public boolean run;
	public static final int MaxDatagramSize = 8192;
	
	
	/*
	 * ������
	 */
	public static void main(String[] args) {
		
	}
	
	
	/*
	 * ���캯��
	 */
	UDPServer(int port) {
//		this.server = server;
		this.port = port;
		run = true;
		try {
			InetAddress IP = InetAddress.getLocalHost();
			ds = new DatagramSocket(port, IP);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}   
	}
	
	
	/*
	 * ��������
	 */
	public void run() {
		while(run) {
			DatagramPacket dp = new DatagramPacket(new byte [MaxDatagramSize], MaxDatagramSize);
			try {
				ds.receive(dp);
				operate(dp);
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * ����
	 */
	private void operate(DatagramPacket dp) {
		
	}
	
}



