package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginFrame {

	private JFrame frame;
	protected JTextField textField;
	protected JPasswordField passwordField;
	protected JButton btnNewButton_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LoginFrame window = new LoginFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int flag = JOptionPane.showConfirmDialog(null, "确定要退出？", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
				if(flag == JOptionPane.YES_OPTION) {
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} 
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 290, 232);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(74, 73, 182, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(74, 119, 182, 21);
		frame.getContentPane().add(passwordField);
		
		JButton btnNewButton = new JButton("login");
		btnNewButton.addActionListener(new LoginListener(this));
		btnNewButton.setBounds(20, 150, 93, 23);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("account");
		lblNewLabel.setBounds(10, 76, 54, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("passward");
		lblNewLabel_1.setBounds(10, 122, 54, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		btnNewButton_1 = new JButton("register");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Client client = new Client(8080);
				RegisterFrame refr = new RegisterFrame(client);
			}
		});
		btnNewButton_1.setBounds(154, 150, 93, 23);
		frame.getContentPane().add(btnNewButton_1);
	}
	
	
	/*
	 * 设置可见性
	 */
	public void setVisible(boolean arg0) {
		frame.setVisible(arg0);
	}
	
}


class LoginListener implements ActionListener {
	LoginFrame frame;
	LoginListener(LoginFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {  //登录流程
		String acc = frame.textField.getText();
		String pwd = frame.passwordField.getText();
		
		if(acc.equals("")) {
			JOptionPane.showMessageDialog(null, "请输入账号！", "提示", JOptionPane.WARNING_MESSAGE);
			return ;
		} else if(pwd.equals("")) {
			JOptionPane.showMessageDialog(null, "请输入密码！", "提示", JOptionPane.WARNING_MESSAGE);
			return ;
		}
		
		try {
			Client client = new Client(8080);
			client.login(acc, pwd, frame);  //登录
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "无法连接服务器！", "提示", JOptionPane.WARNING_MESSAGE);
			return ;
		}
	}
	
}
