package com.oocl.chatserver.server;

import com.oocl.chatserver.thread.LoginServerThread;
import com.oocl.chatserver.thread.RegisterServerThread;
import com.oocl.chatserver.thread.ServerThread;
import com.oocl.protocol.Action;
import com.oocl.protocol.Protocol;

/**
 * 服务器 
 * @author GANAB
 *
 */
public class Server {

	/**
	 * 服务器线程
	 */
	private ServerThread serverThread;
	/**
	 * 注册中心
	 */
	private RegisterServerThread registerServerThread;

	/**
	 * 登录中心
	 */
	private LoginServerThread loginServerThread;
	
	public Server(){}

	public void startServer() {
		this.registerServerThread = new RegisterServerThread();
		registerServerThread.setFlagRun(true);
		registerServerThread.start();
		
		this.loginServerThread = new LoginServerThread();
		loginServerThread.setFlagRun(true);
		loginServerThread.start();
		
		this.serverThread = new ServerThread();
		serverThread.setFlagRun(true);
		serverThread.start();
	}

	public void stopServer(){
		synchronized (serverThread.getMessages()) {
			Protocol response = new Protocol(Action.Exit, "server");
			serverThread.getMessages().add(response);
		}
		serverThread.stopServer();
	}

	public void close() {
		if(serverThread != null){
			if(serverThread.isAlive()){
				serverThread.stopServer();
			}
		}
		System.exit(0);
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.startServer();
	}
}
