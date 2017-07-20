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

import com.oocl.chatserver.service.UserService;
import com.oocl.chatserver.service.impl.UserServiceImpl;
import com.oocl.chatserver.util.ServerConfig;
import com.oocl.protocol.Action;
import com.oocl.protocol.Protocol;

/**
 * 服务器线程
 * 
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
	 * 登录令牌
	 */
	private Hashtable<String, String> tokens;

	/**
	 * 是否退出循环
	 */
	private boolean flagRun = false;

	/**
	 * 发送消息线程
	 */
	private SendThread sendThread;

	/**
	 * 令牌消息线程
	 */
	private TokenThread tokenThread;


	public ServerThread() {
		clients = new Hashtable<String, ClientThread>();
		tokens = new Hashtable<String, String>();
		messages = new Vector<Protocol>();
		try {
			serverSocket = new ServerSocket(Integer.parseInt(ServerConfig.getInstance().CHAT_SERVER_PORT));
			System.out.println("[ServerThread start]Host:"+ServerConfig.getInstance().CHAT_SERVER_HOST
					+" Port:"+ServerConfig.getInstance().CHAT_SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 开启统一发送线程
		sendThread = new SendThread(this);
		sendThread.setFlagRun(true);
		sendThread.start();
		System.out.println("[SendThread start]");
		// 开启令牌线程
		tokenThread = new TokenThread(this);
		tokenThread.setFlagRun(true);
		tokenThread.start();
		System.out.println("[TokenThread start]");

	}

	/**
	 * 根据Token 登录 TODO..接收HttpServer的结束指令。
	 */
	@Override
	public void run() {
		Socket socket;
		while (flagRun) {
			try {
				if (serverSocket.isClosed()) {
					flagRun = false;
				} else {
					try {
						socket = serverSocket.accept();
					} catch (SocketException e) {
						socket = null;
						flagRun = false;
					}
					// 根据Token 处理登录
					if (socket != null) {
						ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						Object o = ois.readObject();
						if (o != null) {
							Protocol p = Protocol.fromJson((String) o);
							if (p != null) {
								if (p.getAction() == Action.Login) {
									String userName = p.getFrom();
									String token = p.getMsg();
									Protocol response = null;
									if (checkToken(userName, token)) {
										// 构造成功返回
										response = new Protocol(Action.Login,"Server", userName, "success", new Date().getTime());
										oos.writeObject(response.toJson());
										oos.flush();

										// 构造通知所有人的登录消息
										Protocol notify = new Protocol(Action.NotifyLogin, userName,"all", userName + " online……", new Date().getTime());
										// 建立线程
										ClientThread clientThread = new ClientThread(socket, this, oos, ois);
										clientThread.setFlagRun(true);
										clients.put(userName, clientThread);// 添加用户线程

										// 构造通知所有人更新在线人数
										Protocol notifyUpdateOnline = new Protocol(Action.List, "server", "all",new Date().getTime());
										StringBuilder sb = new StringBuilder();
										for (String s : clients.keySet()) {
											sb.append(s + ",");
										}
										notifyUpdateOnline.setMsg(sb.toString().substring(0, sb.length() - 1));
										clientThread.start();// 启动客户端线程处理用户输入
										synchronized (messages) {
											messages.addElement(notify);
											messages.addElement(notifyUpdateOnline);
										}
									} else {
										// 无效token
										response = new Protocol(Action.Login,"Server", userName, "failure",new Date().getTime());
										oos.writeObject(response.toJson());
										oos.flush();
									}
								}
							}
						}
					}
				}
			} catch (IOException e) {
				System.out.println("用户异常退出");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopServer() {
		try {
			if (this.isAlive()) {
				tokenThread.setFlagRun(false);
				sendThread.setFlagRun(false);
				serverSocket.close();
				setFlagRun(false);
			}
		} catch (Throwable e) {
		}
	}

	/**
	 * 校验token
	 * 
	 * @param userName
	 * @param token
	 * @return
	 */
	private boolean checkToken(String userName, String token) {
		String tokenFromMap = tokens.get(userName);
		if (token.equals(tokenFromMap)) {
			return true;
		}
		return false;
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

	public Hashtable<String, String> getTokens() {
		return tokens;
	}

}
