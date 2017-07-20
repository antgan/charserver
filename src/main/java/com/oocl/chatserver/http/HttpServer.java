package com.oocl.chatserver.http;

import com.oocl.chatserver.http.thread.HttpServerThread;
import com.oocl.chatserver.server.Server;

/**
 * Http server
 * @author GANAB
 *
 */
public class HttpServer {
	private Server server;
	
	private HttpServerThread httpServerThread;
	
	public HttpServer(Server server) {
		this.server = server;
	}
	
	public void startServer(){
		httpServerThread = new HttpServerThread(server);
		httpServerThread.setFlagRun(true);
		httpServerThread.start();
	}
}
