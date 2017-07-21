package com.oocl.chatserver.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import com.oocl.protocol.Action;
import com.oocl.protocol.Protocol;

/**
 * 客户线程
 * 
 * @author GANAB
 * 
 */
public class ClientThread extends Thread {

	/**
	 * 用户socket
	 */
	private Socket clientSocket;
	/**
	 * 服务器线程
	 */
	private ServerThread serverThread;

	/**
	 * 对象InputStream
	 */
	private ObjectInputStream ois;

	/**
	 * 对象OutputStream
	 */
	private ObjectOutputStream oos;

	private boolean flagRun = false;

	public ClientThread(Socket socket, ServerThread serverThread, ObjectOutputStream oos, ObjectInputStream ois) {
		this.clientSocket = socket;
		this.serverThread = serverThread;
		this.oos = oos;
		this.ois = ois;
	}


	/**
	 * 只负责接收用户输入，并存入全局消息队列Message
	 */
	@Override
	public void run() {
		// 当Flag等于True直接接收用户输入
		while (flagRun) {
			Protocol protocol = null;
			try {
				Object o = ois.readObject();
				if (o != null) {
					protocol = Protocol.fromJson((String)o);
					
					if (protocol.getAction() == Action.Logout) {
						String userName = protocol.getFrom();
						ClientThread ct = serverThread.getClients().get(userName);
						//移除用户线程列表
						serverThread.getClients().remove(userName);
						//关闭线程资源
						ct.closeClienthread();
						//通知所有人某某人登出
						Protocol notify = new Protocol(Action.NotifyLogout, userName, "all", userName+" logout", new Date().getTime());
						serverThread.getMessages().add(notify);
						//通知所有人更新在线列表
						updateOnline(new Protocol(Action.List, "server"), "all");
					}
					if(protocol.getAction() == Action.Chat || protocol.getAction() == Action.Shake){
						serverThread.getMessages().add(protocol);
					}
					//用户要求更新在线用户
					if(protocol.getAction() == Action.List){
						updateOnline(protocol, protocol.getFrom());
					}
				}else{
					continue;
				}
			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
				flagRun = false;
			} catch (IOException e) {
//				e.printStackTrace();
				flagRun = false;
			}
		}
	}
	
	/**
	 * 构建更新在线用户的消息
	 * @param protocol
	 */
	private void updateOnline(Protocol protocol,String to){
		StringBuilder sb = new StringBuilder();
		for(String userName : serverThread.getClients().keySet()){
			sb.append(userName+",");
		}
		protocol.setTo(to);
		protocol.setFrom("server");
		if(sb.length() == 0){
			protocol.setMsg("");
		}else{
			protocol.setMsg(sb.toString().substring(0, sb.length()-1));
		}
		protocol.setTime(new Date().getTime());
		serverThread.getMessages().add(protocol);
	}

	/**
	 * 关闭客户线程
	 * 
	 * @param clientThread
	 */
	public void closeClienthread() {
		if (this.clientSocket != null) {
			try {
				this.clientSocket.close();
			} catch (IOException e) {
				System.out.println("server's clientSocket is null");
			}
		}

		try {
			this.setFlagRun(false);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}

	public void setFlagRun(boolean b) {
		this.flagRun = b;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public boolean isFlagRun() {
		return flagRun;
	}

}
