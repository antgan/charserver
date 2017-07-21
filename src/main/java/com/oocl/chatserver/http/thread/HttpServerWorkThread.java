package com.oocl.chatserver.http.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.oocl.chatserver.http.request.HttpRequest;
import com.oocl.chatserver.http.service.AdminService;
import com.oocl.chatserver.http.service.impl.AdminServiceImpl;
import com.oocl.chatserver.server.Server;
import com.oocl.chatserver.util.ParserUtil;

/**
 * 01 InputStream to MyServletRequest;02 OutputStream to MyServletResponse
 * 
 * @author HEZE2
 * 
 */
public class HttpServerWorkThread extends Thread {
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private ChatThread chatThread;
	private AdminService adminService;
	private HttpServerThread httpThread;

	public HttpServerWorkThread(HttpServerThread httpThread, ChatThread chatThread, Socket socket) throws IOException {
		this.socket = socket;
		this.chatThread = chatThread;
		this.httpThread = httpThread;
		in = socket.getInputStream();
		out = socket.getOutputStream();
		this.adminService = new AdminServiceImpl(chatThread);
	}

	@Override
	public void run() {
		HttpRequest req = ParserUtil.parse(in);
	
		if("GET".equals(req.getMethod())){
			//返回页面 
			if("/admin".equals(req.getPath()) && req.getParams().size() == 0){
				adminService.outputHtml(out);;
			}else if("/admin".equals(req.getPath()) && req.getParams().size() > 0){
				if("start".equals(req.getParams().get("action"))){
					if(chatThread==null){
						chatThread = new ChatThread(new Server());
						httpThread.setChatThread(chatThread);
						chatThread.setFlagRun(true);
						chatThread.start();
						
					}
					adminService.setChatThread(chatThread);
					adminService.outputHtml(out);;
				}else if("stop".equals(req.getParams().get("action"))){
					if(chatThread!=null){
						chatThread.stopThread();
						httpThread.setChatThread(null);
					}
					adminService.outputHtml(out);;
				}
			}
		}
		
		try {
			if (!socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
