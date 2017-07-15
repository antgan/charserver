package com.oocl.chatserver.server;

import com.oocl.chatserver.thread.ServerThread;
import com.oocl.protocol.Action;
import com.oocl.protocol.Protocol;

/**
 * 服务器 
 * @author GANAB
 *
 */
public class Server {

	private ServerThread serverThread;

	public Server(){}

	public void startServer() {
		try{
			this.serverThread = new ServerThread();
		}catch(Exception e){
			System.exit(0);
		}
		serverThread.setFlagRun(true);
		serverThread.start();
		System.out.println("服务器已开启……等待用户连接……");
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
