package com.oocl.chatserver;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.oocl.protocol.Action;
import com.oocl.protocol.Protocol;

/**
 * 单元测试
 * @author GANAB
 *
 */
public class TestFrame extends JFrame implements ActionListener {
	private JTextField actionTf;
	private JTextField fromTf;
	private JTextField toTf;
	private JTextField contentTf;
	private JButton sendBtn;
	private JScrollPane infSp;
	private JTextArea infTa;
	private String titleHead = "Junit Test Frame";
	private String userName;
	private Thread thread;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean flag = true;
	
	public TestFrame(String userName) {
		this.setTitle(titleHead);
		this.userName = userName;
		this.setSize(400, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		class MyWindowAdapter extends WindowAdapter{
			public TestFrame frame;
			
			public MyWindowAdapter(TestFrame frame) {
				this.frame = frame;
			}
			@Override
			public void windowClosing(WindowEvent e) {
				if(frame!=null){
					try {
						frame.flag = false;
						frame.out.close();
						frame.in.close();
						frame.socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		};
		this.addWindowListener(new MyWindowAdapter(this));
		
		init();
		addEvent();
		
		new Thread(){
			public void run() {
				connect();
				while(flag){
					try {
						Object o=in.readObject();
						infTa.append(o.toString()+"\n");
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("客户已退出");
						break;
					}
				}
			};
		}.start();
		this.setVisible(true);
	}
	/**
	 * 
	 */
	private void init(){
		this.setTitle(titleHead+"[UserName: "+this.userName+" ]");
		this.setLayout(null);
		actionTf=new JTextField();
		fromTf=new JTextField();
		toTf=new JTextField();
		contentTf=new JTextField();
		sendBtn=new JButton("Send");
		infTa=new JTextArea(10,20);
		infSp = new JScrollPane(infTa);
		JLabel lab1=new JLabel("Action:");
		JLabel lab2=new JLabel("from:");
		JLabel lab3=new JLabel("to:");
		JLabel lab4=new JLabel("msg:");
		
		lab1.setBounds(80,10,50,50);
		actionTf.setBounds(140, 20, 200, 30);
		lab2.setBounds(80, 50, 50, 50);
		fromTf.setText(this.userName);
		fromTf.setBounds(140, 60, 200, 30);
		lab3.setBounds(80, 90, 50, 50);
		toTf.setBounds(140, 100, 200, 30);
		lab4.setBounds(80, 130, 50, 50);
		contentTf.setBounds(140, 140, 200, 30);
		
		sendBtn.setBounds(240, 180, 100, 30);
		infSp.setBounds(10, 230, 380, 280);
		infTa.setFont(new Font("微软雅黑", Font.BOLD, 12));
		infTa.setLineWrap(true);
		this.add(lab1);
		this.add(actionTf);
		this.add(lab2);
		this.add(fromTf);
		this.add(lab3);
		this.add(toTf);
		this.add(lab4);
		this.add(contentTf);
		this.add(sendBtn);
		this.add(infSp);
	}
	public void connect(){
		try {
			socket=new Socket("127.0.0.1",8889);
			out=new ObjectOutputStream(socket.getOutputStream());
			in=new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void addEvent(){
		sendBtn.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Protocol p= null;
		String action = actionTf.getText();
		if("Login".equals(action)){
			p = new Protocol(Action.Login, userName);
		}else if("Logout".equals(action)){
			p = new Protocol(Action.Logout, userName);
		}else if("Shake".equals(action)){
			p = new Protocol(Action.Shake, userName);
		}else if("Chat".equals(action)){
			p = new Protocol(Action.Chat, userName);
		}else if("List".equals(action)){
			p = new Protocol(Action.List, userName);
		}
		if(p!=null){
			p.setTo(toTf.getText());
			p.setMsg(contentTf.getText());
			p.setTime(new Date().getTime());
			System.out.println(p);
			try {
				out.writeObject(p);
				out.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new TestFrame("Weir");
	}
}






