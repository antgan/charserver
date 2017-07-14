package com.oocl.chatserver.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.oocl.protocol.Action;
import com.oocl.protocol.Protocol;


/**
 * 服务器线程
 * @author GANAB
 *
 */
public class ServerThread extends Thread {
	/**
	 * 服务器Socket
	 */
	private ServerSocket serverSocket;
	/**
	 * 客户列表<userName, ClientThread>
	 */
	private Hashtable<String, ClientThread> clients;
	/**
	 * 任务队列
	 */
	private Vector<Protocol> messages;
	/**
	 * 端口
	 */
	private int port = 5000;
	
	/**
	 * 是否退出循环
	 */
	private boolean flagRun = false;
	
	/**
	 * 发送消息线程
	 */
	private SendThread sendThread;
	
	public ServerThread() {
		clients = new Hashtable<String, ClientThread>();
		messages = new Vector<Protocol>();
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendThread = new SendThread(this);
		sendThread.setFlagRun(true);
		sendThread.start();
	}
	
	/**
	 * 只管用户clients的存储
	 */
	@Override
	public void run() {
		Socket socket;
		while(flagRun){
				try {
					if(serverSocket.isClosed()){
						flagRun = false;
					}else{
						try{
							socket = serverSocket.accept();
						}catch(SocketException e){
							socket = null;
							flagRun = false;
						}
						//检查是否存在用户
						if(socket != null){
							ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
							ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
							Object o = ois.readObject();
							if(o != null){
								Protocol p = (Protocol)o;
								if(p!=null){
									String userName = p.getFrom();
									Protocol response = null;
									//如果已经存在,返回登录失败。如果不存在，创建线程
									if(clients.containsKey(userName)){
										System.out.println(userName+ "已经存在");
										response = new Protocol(Action.Login, "server", userName, "exist", new Date().getTime());
										oos.writeObject(response);
										oos.flush();
									}else{
										//构造成功返回
										response = new Protocol(Action.Login, "server", userName, "success", new Date().getTime());
										oos.writeObject(response);
										oos.flush();
										System.out.println(userName+ "登录成功");	
										//构造通知所有人的登录消息
										Protocol notify = new Protocol(Action.NotifyLogin, userName, "all", userName + " online……", new Date().getTime());
										//建立线程
										ClientThread clientThread = new ClientThread(socket, this,oos,ois);
										clientThread.setFlagRun(true);
										clients.put(userName, clientThread);//添加用户线程
										
										//构造通知所有人更新在线人数
										Protocol notifyUpdateOnline = new Protocol(Action.List, "server", "all", new Date().getTime());
										StringBuilder sb = new StringBuilder();
										for(String s : clients.keySet()){
											sb.append(s+",");
										}
										notifyUpdateOnline.setMsg(sb.toString().substring(0, sb.length()-1));
										synchronized (messages) {
											messages.addElement(notify);
											messages.addElement(notifyUpdateOnline);
										}
										
										clientThread.start();//启动客户端线程处理用户输入
									}
								}
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
		}
	}
	
	public void stopServer() {
		try {
			if(this.isAlive()){
				serverSocket.close();
				setFlagRun(false);
			}
		} catch (Throwable e) {}
	}


	public Hashtable<String, ClientThread> getClients() {
		return clients;
	}

	public Vector<Protocol> getMessages() {
		return messages;
	}

	public boolean isFlagRun() {
		return flagRun;
	}

	public void setFlagRun(boolean flagRun) {
		this.flagRun = flagRun;
	}


	
}
