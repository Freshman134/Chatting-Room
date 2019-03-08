package Client;


import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.DatagramPacket;


public class UDPClient {
	DatagramSocket ds;
	InetAddress serverIP;
	public static final int MaxDatagramSize = 8192;
	
	
	/*
	 * 测试用
	 */
	public static void main(String [] args) {
		
	}
	
	
	/*
	 * 构造函数
	 */
	UDPClient() {
		try {
			serverIP = InetAddress.getLocalHost();
			ds = new DatagramSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * 发送并接收回应
	 */
	public String request(String message) throws IOException {
		String request = null;
		byte [] buf = message.getBytes();
		DatagramPacket dp = new DatagramPacket(buf, buf.length, serverIP, 8081);
		ds.send(dp);
		
		dp = new DatagramPacket(new byte [MaxDatagramSize], MaxDatagramSize);
		ds.receive(dp);
		request = new String(dp.getData());
		
		return request;
	}
	
}
