package Client;

import java.awt.EventQueue;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserFrame {
	Client client;
	String account;
	String passward;
	public HashMap<String, String> friendSocketList;
	private JFrame frmQq;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserFrame window = new UserFrame();
					window.frmQq.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserFrame(Client client) {
		this.client = client;
		beforeInitalize();
		initialize();
	}
	
	
	public UserFrame() {
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmQq = new JFrame();
		frmQq.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int flag = JOptionPane.showConfirmDialog(null, "ȷ��Ҫ�˳���", "��ʾ", JOptionPane.YES_NO_CANCEL_OPTION);
				if(flag == JOptionPane.YES_OPTION) {
					frmQq.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} 
			}
		});
		frmQq.setTitle("ChattingRoom");
		frmQq.setResizable(false);
		frmQq.setBounds(100, 100, 260, 482);
		frmQq.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmQq.getContentPane().setLayout(null);
	}
	
	
	/*
	 * ���������ȡ��������ǰ����Ҫ����Ϣ
	 */
	private void beforeInitalize() {
		client.getPersonalData(this);
	}
	
	
	/*
	 * �����Ƿ�ɼ��ɼ�
	 */
	public void setVisible(boolean arg0) {
		frmQq.setVisible(arg0);
	}
	
	
	/*
	 * �����ʼ��
	 */
	public void componmentInitalize() {
		
	}
	
}
