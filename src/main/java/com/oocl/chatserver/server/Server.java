package com.oocl.chatserver.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oocl.chatserver.pojo.User;
import com.oocl.chatserver.service.UserService;
import com.oocl.chatserver.service.impl.UserServiceImpl;
import com.oocl.chatserver.thread.LoginServerThread;
import com.oocl.chatserver.thread.RegisterServerThread;
import com.oocl.chatserver.thread.ServerThread;
import com.oocl.chatserver.util.StringUtil;

/**
 * 服务集群
 * @author GANAB
 *
 */
public class Server {

	/**
	 * 聊天服务器
	 */
	private ServerThread serverThread;
	/**
	 * 注册中心
	 */
	private RegisterServerThread registerServerThread;

	/**
	 * 登录中心
	 */
	private LoginServerThread loginServerThread;
	
	private long startTime;
	
	private UserService userService;
	
	public Server(){
		userService = new UserServiceImpl();
		startTime = System.currentTimeMillis();
	}

	public void startServer() {
		this.registerServerThread = new RegisterServerThread();
		registerServerThread.setFlagRun(true);
		registerServerThread.start();
		this.loginServerThread = new LoginServerThread();
		loginServerThread.setFlagRun(true);
		loginServerThread.start();
		this.serverThread = new ServerThread();
		serverThread.setFlagRun(true);
		serverThread.start();
		startTime = System.currentTimeMillis();
	}

	public void stopServer(){
		serverThread.stopServer();
		registerServerThread.stopServer();
		loginServerThread.stopServer();
	}

	public void close() {
		if(serverThread != null){
			if(serverThread.isAlive()){
				serverThread.stopServer();
			}
		}
		System.exit(0);
	}
	
	public List<String> getOnlines(){
		List<String> onlines = new ArrayList<String>();
		if(serverThread!=null){
			Set<String> keys = serverThread.getClients().keySet();
			for(String key : keys){
				onlines.add(key);
			}
		}

		return onlines;
	}
	
	public List<String> getRegisters(){
		List<User> users = userService.queryAllUsers();
		List<String> registers = new ArrayList<String>();
		for(User u : users){
			registers.add(u.getName());
		}
		return registers;
	}
	
	public Map<String,String> getTokens(){
		if(serverThread!=null){
			return serverThread.getTokens();
		}
		return new HashMap<String, String>();
	}
	
	public String getStartTime(){
		return StringUtil.getFormatDateStr(startTime);
	}
	
	public String getRunningTime(){
		long now = System.currentTimeMillis();
		long t = (now - startTime)/1000;
		return t+"s";
	}
	
	
	public static void main(String[] args) throws Exception{
		Server server = new Server();
		server.startServer();
	}
	
	
}
