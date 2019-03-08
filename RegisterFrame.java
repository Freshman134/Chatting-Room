package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterFrame {
	
	public Client client;
	private JFrame frmRegistration;
	private JTextField textField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterFrame window = new RegisterFrame();
					window.frmRegistration.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RegisterFrame() {
		initialize();
	}
	
	public RegisterFrame(Client client) {
		this.client = client;
		initialize();
		frmRegistration.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRegistration = new JFrame();
		frmRegistration.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int flag = JOptionPane.showConfirmDialog(null, "确定要退出？", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
				if(flag == JOptionPane.YES_OPTION) {
					frmRegistration.setVisible(false);
				} 
			}
		});
		frmRegistration.setTitle("Registration");
		frmRegistration.setResizable(false);
		frmRegistration.setBounds(100, 100, 450, 300);
		frmRegistration.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmRegistration.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(147, 74, 245, 21);
		frmRegistration.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u8BF7\u8F93\u5165\u65B0\u8D26\u53F7\uFF1A");
		lblNewLabel.setBounds(44, 77, 93, 15);
		frmRegistration.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("\u8BF7\u8F93\u5165\u5BC6\u7801\uFF1A");
		lblNewLabel_1.setBounds(44, 123, 93, 15);
		frmRegistration.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("\u8BF7\u786E\u8BA4\u5BC6\u7801\uFF1A");
		lblNewLabel_2.setBounds(44, 169, 93, 15);
		frmRegistration.getContentPane().add(lblNewLabel_2);
		
		JButton btnNewButton = new JButton("\u6CE8   \u518C");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String acc = textField.getText();
				String pwd = passwordField.getText();
				String rppwd = passwordField_1.getText();
				if(acc.equals("")) {
					JOptionPane.showMessageDialog(null, "请输入账号", "警告", JOptionPane.ERROR_MESSAGE);
				} else if (pwd.equals("")) {
					JOptionPane.showMessageDialog(null, "请输入密码", "警告", JOptionPane.ERROR_MESSAGE);
				} else if (rppwd.equals("")) {
					JOptionPane.showMessageDialog(null, "请输入确认密码", "警告", JOptionPane.ERROR_MESSAGE);
				}
				if(pwd.equals(rppwd)) {
					client.register(acc, pwd);
				} else {
					//弹出警告窗口
					JOptionPane.showMessageDialog(null, "密码输入不一致", "警告", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton.setBounds(174, 219, 93, 23);
		frmRegistration.getContentPane().add(btnNewButton);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(147, 120, 245, 21);
		frmRegistration.getContentPane().add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(147, 166, 245, 21);
		frmRegistration.getContentPane().add(passwordField_1);
	}
}
