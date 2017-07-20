package com.oocl.chatserver.dao;

import java.util.List;

import com.oocl.chatserver.pojo.User;

public interface UserDao {

	public int addUser(User user);
	
	public int deleteUser(Integer id);
	
	public int updateUser(User user);
	
	public List<User> queryAllUser();
	
	public User queryUserById(Integer id);
	
	public User queryUserByNameAndPwd(String name, String pwd);
	
	public boolean isExist(String name);
}
