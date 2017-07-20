package com.oocl.chatserver.service;

import java.util.List;

import com.oocl.chatserver.pojo.User;

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
	
	/**
	 * 查询所有注册用户
	 * @return
	 */
	public List<User> queryAllUsers();
	
}
