package com.oocl.chatserver.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.oocl.protocol.Action;
import com.oocl.protocol.Protocol;

/**
 * 处理接收登录中心的用户Token令牌
 * @author GANAB
 *
 */
public class TokenThread extends Thread{
	private static final String HOST_URL = "127.0.0.1";
	private static final int PORT = 8887;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private ServerThread serverThread;
	private boolean flagRun = false;
	
	public TokenThread(ServerThread serverThread) {
		try {
			socket = new Socket(HOST_URL, PORT);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			//与登录中心建立连接
			connectLoginServer();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.serverThread = serverThread;
	}
	
	private void connectLoginServer() throws IOException{
		Protocol request = new Protocol(Action.Token, "Token thread");
		oos.writeObject(request.toJson());
		oos.flush();
	}
	
	@Override
	public void run() {
		if(socket.isClosed()){
			flagRun =false;
		}
		while(flagRun){
			Protocol request = null;
			try {
				Object o = ois.readObject();
				if(o!=null){
					request = Protocol.fromJson((String)o);
				}else{
					continue;
				}
				
				if(Action.Token == request.getAction()){
					String userName = request.getTo();
					String token = request.getMsg();
					System.out.println("Receive Token : "+token+" UserName: "+userName);
					synchronized (serverThread.getTokens()) {
						//放入token
						serverThread.getTokens().put(userName, token);
					}
				}
					
			} catch (ClassNotFoundException e) {
				flagRun = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setFlagRun(boolean flagRun) {
		this.flagRun = flagRun;
	}
	
	
}
