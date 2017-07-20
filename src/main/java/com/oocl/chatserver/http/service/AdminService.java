package com.oocl.chatserver.http.service;

import java.io.OutputStream;

public interface AdminService {

	public void start();
	
	public void stop();
	
	public void outputHtml(OutputStream out);
}
