package com.oocl.chatserver;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.oocl.protocol.Protocol;


public class TestFrame extends JFrame implements ActionListener {
	private JTextField actionTf;
	private JTextField fromTf;
	private JTextField toTf;
	private JTextField contentTf;
	private JButton sendBtn;
	private JTextArea infTa;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public TestFrame() {
		this.setSize(320, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		addEvent();
		
		new Thread(){
			public void run() {
				while(true){
					connect();
					try {
						Object o=in.readObject();
						infTa.append(o.toString()+"\n");
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		this.setVisible(true);
	}
	private void init(){
		this.setLayout(null);
		actionTf=new JTextField();
		fromTf=new JTextField();
		toTf=new JTextField();
		contentTf=new JTextField();
		sendBtn=new JButton("发送");
		infTa=new JTextArea(10,20);
		JLabel lab1=new JLabel("Action:");
		JLabel lab2=new JLabel("from:");
		JLabel lab3=new JLabel("to:");
		JLabel lab4=new JLabel("msg:");
		
		lab1.setBounds(20,10,50,50);
		actionTf.setBounds(80, 20, 200, 30);
		lab2.setBounds(20, 50, 50, 50);
		fromTf.setBounds(80, 60, 200, 30);
		this.add(lab1);
		this.add(actionTf);
		this.add(lab2);
		this.add(fromTf);
		this.add(lab3);
		this.add(toTf);
		this.add(lab4);
		this.add(contentTf);
		this.add(sendBtn);
		this.add(infTa);
	}
	public void connect(){
		try {
			socket=new Socket("127.0.0.1",5000);
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
//		p.setAction(Integer.parseInt(actionTf.getText()));
//		p.setFrom(fromTf.getText());
//		p.setTo(toTf.getText());
//		p.setContent(contentTf.getText());
		try {
			out.writeObject(p);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TestFrame();
	}
}






