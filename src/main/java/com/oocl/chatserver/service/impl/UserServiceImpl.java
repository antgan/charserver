package com.oocl.chatserver.service.impl;

import com.oocl.chatserver.dao.UserDao;
import com.oocl.chatserver.dao.impl.UserDaoImpl;
import com.oocl.chatserver.pojo.User;
import com.oocl.chatserver.service.UserService;

/**
 * User service Impl
 * @author GANAB
 *
 */
public class UserServiceImpl implements UserService {
	private UserDao userDao;
	
	public UserServiceImpl() {
		userDao = new UserDaoImpl();
	}
	
	public boolean register(String name, String pwd) {
		//check exist
		if(!userDao.isExist(name)){
			userDao.addUser(new User(name, pwd));
			return true;
		}
		return false;
	}

	public boolean login(String name, String pwd) {
		User user = userDao.queryUserByNameAndPwd(name, pwd);
		if(user!=null){
			return true;
		}
		return false;
	}

}
