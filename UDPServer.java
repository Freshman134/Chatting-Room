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
	 * 测试用
	 */
	public static void main(String[] args) {
		
	}
	
	
	/*
	 * 构造函数
	 */
	UDPServer(int port) {
//		this.server = server;
		this.port = port;
		run = true;
		try {
			InetAddress IP = InetAddress.getLocalHost();
			ds = new DatagramSocket(port, IP);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}   
	}
	
	
	/*
	 * 监听进行
	 */
	public void run() {
		while(run) {
			DatagramPacket dp = new DatagramPacket(new byte [MaxDatagramSize], MaxDatagramSize);
			try {
				ds.receive(dp);
				operate(dp);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * 运行
	 */
	private void operate(DatagramPacket dp) {
		
	}
	
}



