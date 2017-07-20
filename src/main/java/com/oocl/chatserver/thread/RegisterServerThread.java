package com.oocl.chatserver.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.oocl.chatserver.service.UserService;
import com.oocl.chatserver.service.impl.UserServiceImpl;
import com.oocl.chatserver.util.ServerConfig;
import com.oocl.protocol.Action;
import com.oocl.protocol.Protocol;

/**
 * 注册服务线程
 * 
 * @author GANAB
 * 
 */
public class RegisterServerThread extends Thread {
	/**
	 * 服务器Socket
	 */
	private ServerSocket serverSocket;

	/**
	 * 是否退出循环
	 */
	private boolean flagRun = false;

	/**
	 * 用户业务处理
	 */
	private UserService userService;

	public RegisterServerThread() {
		try {
			serverSocket = new ServerSocket(Integer.parseInt(ServerConfig.getInstance().REGISTER_SERVER_PORT));
			userService = new UserServiceImpl();
			System.out.println("[RegisterServerThread start]Host:"+ServerConfig.getInstance().REGISTER_SERVER_HOST
					+" Port:"+ServerConfig.getInstance().REGISTER_SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 只管用户注册
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
						//建立注册Socket
						socket = serverSocket.accept();
					} catch (SocketException e) {
						socket = null;
						flagRun = false;
					}
					//  处理登录注册问题……
					if (socket != null) {
						ObjectInputStream ois = new ObjectInputStream(
								socket.getInputStream());
						ObjectOutputStream oos = new ObjectOutputStream(
								socket.getOutputStream());
						Object o = ois.readObject();
						if (o != null) {
							Protocol p = Protocol.fromJson((String)o);
							if (p != null) {
								if(p.getAction() == Action.Register){
									String name = p.getFrom().trim();
									String pwd = p.getPwd().trim();
									
									boolean result = userService.register(name, pwd);
									//如果注册成功，返回成功消息，反之。
									if(result){
										p.setMsg("success");
									}else{
										p.setMsg("failure");
									}
									oos.writeObject(p.toJson());
									oos.flush();
								}
							}
						}
					}
				}
			} catch (IOException e) {
				System.out.println("[RegisterServerThread] : 用户异常退出");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopServer() {
		try {
			if (this.isAlive()) {
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
