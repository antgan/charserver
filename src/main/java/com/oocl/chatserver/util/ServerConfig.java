package com.oocl.chatserver.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;



/**
 * 服务器的配置
 * @author GANAB
 *
 */
public class ServerConfig {
	public String LOGIN_SERVER_HOST;
	public String LOGIN_SERVER_PORT;
	public String REGISTER_SERVER_HOST;
	public String REGISTER_SERVER_PORT;
	public String CHAT_SERVER_HOST;
	public String CHAT_SERVER_PORT;
	public String HTTP_SERVER_HOST;
	public String HTTP_SERVER_PORT;
	private static ServerConfig instance;  
	
	private ServerConfig() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("server.properties"));
			LOGIN_SERVER_HOST = properties.getProperty("LOGIN_SERVER_HOST");
			LOGIN_SERVER_PORT = properties.getProperty("LOGIN_SERVER_PORT");
			REGISTER_SERVER_HOST = properties.getProperty("REGISTER_SERVER_HOST");
			REGISTER_SERVER_PORT = properties.getProperty("REGISTER_SERVER_PORT");
			CHAT_SERVER_HOST = properties.getProperty("CHAT_SERVER_HOST");
			CHAT_SERVER_PORT = properties.getProperty("CHAT_SERVER_PORT");
			HTTP_SERVER_HOST = properties.getProperty("HTTP_SERVER_HOST");
			HTTP_SERVER_PORT = properties.getProperty("HTTP_SERVER_PORT");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static synchronized ServerConfig getInstance(){
		if(instance == null){
			instance = new ServerConfig();
		}
		return instance;
	}
}
