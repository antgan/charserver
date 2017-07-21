package com.oocl.chatserver.http.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.oocl.chatserver.html.template.AdminTemplate;
import com.oocl.chatserver.html.template.TemplateParameter;
import com.oocl.chatserver.http.service.AdminService;
import com.oocl.chatserver.http.thread.ChatThread;


public class AdminServiceImpl implements AdminService{
	private ChatThread chatThread;
	private AdminTemplate adminTemplate;
	
	public AdminServiceImpl(ChatThread chatThread) {
		adminTemplate = new AdminTemplate();
		this.chatThread = chatThread;
	}
	
	public void outputHtml(OutputStream out){
		TemplateParameter param = new TemplateParameter();	
			if(chatThread!=null && chatThread.server!=null){
				param.setStartTime(chatThread.server.getStartTime());
				param.setRunningTime(chatThread.server.getRunningTime());
				param.setOnlines(chatThread.server.getOnlines());
				param.setRegisters(chatThread.server.getRegisters());
				param.setTokens(chatThread.server.getTokens());
			}else{
				param.setStartTime("");
				param.setRunningTime("");
				param.setOnlines(new ArrayList<String>());
				param.setRegisters(new ArrayList<String>());
				param.setTokens(new HashMap<String, String>());
			}
			param.setTitile("WTIP Admin server");
			param.setStartAction("/admin?action=start");
			param.setStopAction("/admin?action=stop");
			adminTemplate.build(out, param);
		
	}

	public void setChatThread(ChatThread chatThread) {
		this.chatThread = chatThread;
	}
	
}
