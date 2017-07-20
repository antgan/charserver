package com.oocl.chatserver.dao.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.oocl.chatserver.dao.UserDao;
import com.oocl.chatserver.pojo.User;

public class UserDaoImplTest {
	private UserDao userDao = new UserDaoImpl();
	

	@Test
	public void testAddUser() {
		User user = new User("Zero","123");
		int result = userDao.addUser(user);
		Assert.assertTrue(result == 1);
	}

	@Test
	public void testDeleteUser() {
		int result = userDao.deleteUser(1);
		Assert.assertTrue(result == 1);
	}

	@Test
	public void testUpdateUser() {
		User user = new User("Abel","1234");
		user.setId(2);
		int result = userDao.updateUser(user);
		Assert.assertTrue(result == 1);
	}

	@Test
	public void testQueryAllUser() {
		List<User> list = userDao.queryAllUser();
		for(User u : list){
			System.out.println(u.getName());
		}
	}

	@Test
	public void testQueryUserById() {
		User u = userDao.queryUserById(2);
		Assert.assertTrue(u.getName().equals("Abel"));
	}

	@Test
	public void testIsExist() {
		Assert.assertTrue(userDao.isExist("Zero"));
	}

}
