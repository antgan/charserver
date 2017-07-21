package com.oocl.chatserver.http.service;

import java.io.OutputStream;

import com.oocl.chatserver.http.thread.ChatThread;

public interface AdminService {

	
	public void outputHtml(OutputStream out);
	
	public void setChatThread(ChatThread chatThread);
}
