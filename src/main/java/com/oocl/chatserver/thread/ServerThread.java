package com.oocl.chatserver.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Vector;

import com.oocl.chatserver.protocol.Protocol;

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
	 * 客户列表
	 */
	private Hashtable<String, ClientThread> clients;
	/**
	 * 消息
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
						
						if(socket != null){
							ClientThread clientThread = new ClientThread(socket, this);
							clientThread.setFlagRun(true);
							clientThread.start();//启动客户端线程处理用户输入
						}
					}
				} catch (IOException e) {
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
