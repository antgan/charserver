package com.oocl.chatserver.http.thread;

import com.oocl.chatserver.server.Server;


public class ChatThread extends Thread{
	private boolean flagRun = false;
	public Server server;
	
	public ChatThread(Server server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		server.startServer();
		while(flagRun){}
	}
	
	public void stopThread(){
		server.stopServer();
		this.flagRun = true;
	}

	public void setFlagRun(boolean flagRun) {
		this.flagRun = flagRun;
	}

	
}
