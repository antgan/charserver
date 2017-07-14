package com.oocl.chatserver.thread;

import java.io.IOException;
import java.util.Set;

import com.oocl.protocol.Action;
import com.oocl.protocol.Protocol;


/**
 * 管理发送的线程
 * @author GANAB
 *
 */
public class SendThread extends Thread{

	/**
	 * 服务器线程
	 */
	private ServerThread serverThread;
	private boolean flagRun = false;
	/**
	 * 请求
	 */
	private Protocol message;
	public SendThread(ServerThread serverThread){
		this.serverThread = serverThread;
	}
	
	/**
	 * 私聊/群发操作
	 */
	@Override
	public void run() {
		while(flagRun){
			synchronized (serverThread.getMessages()) {
				if(serverThread.getMessages().isEmpty()){
					continue;
				}else{
					message = (Protocol)serverThread.getMessages().firstElement();
					serverThread.getMessages().removeElement(message);
				}
				
				//发送
				if(message.getAction() == Action.Chat || message.getAction() == Action.Shake || message.getAction() == Action.List){
					if("all".equals(message.getTo())){
						send(message, true);
					}else{
						send(message, false);
					}
				}else if(message.getAction() == Action.NotifyLogin || message.getAction() == Action.NotifyLogout || message.getAction() == Action.Exit){
					send(message, true);
				}
			}
			
		}
	}
	
	
	private void send(Protocol message, boolean isAll){
		try {	
			//群发
			if(isAll){
				Set<String> keySet = serverThread.getClients().keySet();
				for(String key : keySet){
					//群发
					serverThread.getClients().get(key).getOos().writeObject(message);
					serverThread.getClients().get(key).getOos().flush();
				}
			// 单发
			} else {
				String to = message.getTo();
				if(serverThread.getClients().containsKey(to)){
					serverThread.getClients().get(to).getOos().writeObject(message);
					serverThread.getClients().get(to).getOos().flush();
				}
			}
		} catch (IOException e) {
			
		};
	}

	public boolean isFlagRun() {
		return flagRun;
	}

	public void setFlagRun(boolean flagRun) {
		this.flagRun = flagRun;
	}
	
	
}
