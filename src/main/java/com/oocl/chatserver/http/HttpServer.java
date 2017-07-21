package com.oocl.chatserver.http;

import com.oocl.chatserver.http.thread.HttpServerThread;

/**
 * Http server
 * @author GANAB
 *
 */
public class HttpServer {
	private HttpServerThread httpServerThread;
	
	public HttpServer() {}
	
	public void startServer(){
		httpServerThread = new HttpServerThread();
		httpServerThread.setFlagRun(true);
		httpServerThread.start();
	}
	
	public static void main(String[] args) {
		HttpServer httpServer = new HttpServer();
		httpServer.startServer();
		System.out.println("[Http server start]");
	}
}
