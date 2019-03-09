package Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.awt.FlowLayout;

public class ServerFrame {
	private JFrame frame;
	private JList<String> list;
	private JTextField textField;
	private LinkedList<String> users;
	public DefaultListModel<String> dls = new DefaultListModel<>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame window = new ServerFrame();
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
	public ServerFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Server server = new Server(8080, this);
		server.start();
		
		users = new LinkedList<String>();
		
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel label = new JLabel("\u670D\u52A1\u5668\u6B63\u5728\u5F00\u542F");
		label.setFont(new Font("宋体", Font.PLAIN, 30));
		label.setBounds(53, 10, 235, 50);
		frame.getContentPane().add(label);
		
		JPanel panel = new JPanel();
		panel.setBounds(281, 85, 138, 176);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		list = new JList<>();
		list.setBounds(0, 0, 138, 176);
		list.setModel(dls);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 138, 176);
		scrollPane.add(list);
		panel.add(scrollPane);
		
		textField = new JTextField();
		textField.setBounds(65, 130, 192, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String str = textField.getText();
				dls.addElement(str);
			}
		});
		btnNewButton.setBounds(105, 193, 93, 23);
		frame.getContentPane().add(btnNewButton);
		
		
	}
	
	
	/*
	 * 增加用户
	 */
	public void add(String acc) {
		users.add(acc);
		dls.addElement(acc);
	}
	
	
	/*
	 * 查询该用户是否已上线
	 */
	public boolean online(String acc) {
		boolean flag = users.contains(acc);
		return flag;
	}
}
