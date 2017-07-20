package com.oocl.chatserver.http.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.oocl.chatserver.server.Server;
/**
 * service Monitor
 * @author HEZE2
 *
 */
public class HttpServerThread extends Thread{
	private Server server;
	private ServerSocket httpServer;
	
	private boolean flagRun = false;
	/**
	 * not good,using constructor(ServerSocket serverSocket) is better
	 */
	public HttpServerThread(Server server) {
		this.server = server;
		try {
			httpServer = new ServerSocket(9000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public HttpServerThread(ServerSocket serverSocket) {
		this.httpServer = serverSocket;
	}
	
	@Override
	public void run() {
		try {
			while(flagRun){
				Socket socket = httpServer.accept();
				HttpServerWorkThread workThread = new HttpServerWorkThread(socket, server);
				workThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setFlagRun(boolean flagRun) {
		this.flagRun = flagRun;
	}
	
	
}
