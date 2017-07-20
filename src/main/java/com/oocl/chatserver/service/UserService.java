package com.oocl.chatserver.service;

/**
 * User service
 * @author GANAB
 *
 */
public interface UserService {

	/**
	 * Register user
	 * @param name
	 * @param pwd
	 * @return
	 */
	public boolean register(String name, String pwd);
	
	/**
	 * login user
	 * @param name
	 * @param pwd
	 * @return
	 */
	public boolean login(String name, String pwd);
	
}
