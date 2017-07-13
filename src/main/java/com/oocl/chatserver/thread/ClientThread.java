package com.oocl.chatserver.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import com.oocl.chatserver.protocol.Action;
import com.oocl.chatserver.protocol.Protocol;

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

	public ClientThread(Socket socket, ServerThread serverThread) {
		this.clientSocket = socket;
		this.serverThread = serverThread;
		try {
			//阻塞。TODO
			ois = new ObjectInputStream(clientSocket.getInputStream());
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				if (ois.available() > 0) {
					Object o = ois.readObject();
					if (o != null) {
						protocol = (Protocol) o;
						/**
						 * 如果是登录操作
						 */
						if (protocol.getAction() == Action.Login) {
							String userName = protocol.getFrom();
							if (userName != null) {
								if (!serverThread.getClients().contains(userName)) {
									serverThread.getClients().put(userName, this);
								}
							}

							// 构造返回消息
							Protocol response = new Protocol(Action.Login,
								 userName, "all", new Date().getTime());
							synchronized (serverThread.getMessages()) {
								serverThread.getMessages().add(response);
							}
						}

					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭客户线程
	 * 
	 * @param clientThread
	 */
	public void closeClienthread(ClientThread clientThread) {
		if (clientThread.clientSocket != null) {
			try {
				clientThread.clientSocket.close();
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
