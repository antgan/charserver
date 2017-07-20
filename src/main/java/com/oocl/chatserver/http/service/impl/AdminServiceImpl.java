package com.oocl.chatserver.http.service.impl;

import java.io.OutputStream;

import com.oocl.chatserver.html.template.AdminTemplate;
import com.oocl.chatserver.html.template.TemplateParameter;
import com.oocl.chatserver.http.service.AdminService;
import com.oocl.chatserver.server.Server;


public class AdminServiceImpl implements AdminService{
	private Server server;
	private AdminTemplate adminTemplate;
	
	public AdminServiceImpl(Server server) {
		adminTemplate = new AdminTemplate();
		this.server = server;
	}
	
	public void start(){
		System.out.println("Action start");
		server.startServer();
	}
	
	public void stop(){
		System.out.println("Action stop");
		server.stopServer();
	}
	
	public void outputHtml(OutputStream out){
		TemplateParameter param = new TemplateParameter();
		param.setTitile("WTIP Admin server");
		param.setStartTime(server.getStartTime());
		param.setRunningTime(server.getRunningTime());
		param.setStartAction("/admin?action=start");
		param.setStopAction("/admin?action=stop");
		param.setOnlines(server.getOnlines());
		param.setRegisters(server.getRegisters());
		param.setTokens(server.getTokens());
		adminTemplate.build(out, param);
	}
}
