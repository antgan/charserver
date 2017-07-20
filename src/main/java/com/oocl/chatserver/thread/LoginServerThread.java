package com.oocl.chatserver.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Hashtable;

import com.oocl.chatserver.service.UserService;
import com.oocl.chatserver.service.impl.UserServiceImpl;
import com.oocl.chatserver.util.ServerConfig;
import com.oocl.chatserver.util.StringUtil;
import com.oocl.protocol.Action;
import com.oocl.protocol.Protocol;

/**
 * 登录中心
 * 
 * @author GANAB
 * 
 */
public class LoginServerThread extends Thread {
	/**
	 * 服务器Socket
	 */
	private ServerSocket serverSocket;

	/**
	 * 接收令牌的socket
	 */
	private Socket tokenSocket;
	
	private ObjectOutputStream tokenOos;

	/**
	 * token令牌
	 */
	private Hashtable<String, String> tokens;
	
	/**
	 * 是否退出循环
	 */
	private boolean flagRun = false;

	private UserService userService;

	public LoginServerThread() {
		try {
			serverSocket = new ServerSocket(Integer.parseInt(ServerConfig.getInstance().LOGIN_SERVER_PORT));
			userService = new UserServiceImpl();
			tokens = new Hashtable<String, String>();
			System.out.println("[LoginServerThread start]Host:"+ServerConfig.getInstance().LOGIN_SERVER_HOST
					+" Port:"+ServerConfig.getInstance().LOGIN_SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 只校验用户登录及发放令牌
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
					// 处理登录
					if (socket != null) {
						ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						Object o = ois.readObject();
						if (o != null) {
							Protocol p = Protocol.fromJson((String) o);
							if (p != null) {
								// 接收TokenThread发来的连接
								if (Action.Token == p.getAction()) {
									this.tokenSocket = socket;
									this.tokenOos = oos;
									System.out.println("[Token thread connect to Login Server success]");
								} else if (Action.Login == p.getAction()) {
									String userName = p.getFrom();
									String pwd = p.getPwd();
									Protocol response = null;
									//登录成功，查询是否存在令牌
									if (userService.login(userName, pwd)) {
										String token = null;
										if(tokens.containsKey(userName)){
											token = tokens.get(userName);
										}else{
											token = StringUtil.createToken();
											Protocol tokenRequest = new Protocol(Action.Token, "Login Server",userName, new Date().getTime());
											tokenRequest.setMsg(token);
											// 发放令牌
											if (tokenSocket != null) {
												this.tokenOos.writeObject(tokenRequest.toJson());
												this.tokenOos.flush();
											}
											tokens.put(userName, token);
										}
										
										// 构造成功返回
										response = new Protocol(Action.Login, "Login Server", userName, token, new Date().getTime());
										oos.writeObject(response.toJson());
										oos.flush();
									} else {
										// 密码错误
										response = new Protocol(Action.Login, "Login Server", userName, "failure", new Date().getTime());
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
				tokenOos.close();
				tokenSocket.close();
				serverSocket.close();
				setFlagRun(false);
			}
		} catch (Throwable e) {
		}
	}

	public boolean isFlagRun() {
		return flagRun;
	}

	public void setFlagRun(boolean flagRun) {
		this.flagRun = flagRun;
	}

}
