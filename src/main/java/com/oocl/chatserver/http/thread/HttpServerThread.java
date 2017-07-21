package com.oocl.chatserver.http.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.oocl.chatserver.util.ServerConfig;
/**
 * service Monitor
 * @author HEZE2
 *
 */
public class HttpServerThread extends Thread{
	private ServerSocket httpServer;
	
	private boolean flagRun = false;

	private ChatThread chatThread;
	/**
	 * not good,using constructor(ServerSocket serverSocket) is better
	 */
	public HttpServerThread(){ 
		try {
			httpServer = new ServerSocket(Integer.parseInt(ServerConfig.getInstance().HTTP_SERVER_PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while(flagRun){
				Socket socket = httpServer.accept();
				HttpServerWorkThread workThread = new HttpServerWorkThread(this, chatThread, socket);
				workThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setFlagRun(boolean flagRun) {
		this.flagRun = flagRun;
	}

	public ChatThread getChatThread() {
		return chatThread;
	}

	public void setChatThread(ChatThread chatThread) {
		this.chatThread = chatThread;
	}
	
	
}
