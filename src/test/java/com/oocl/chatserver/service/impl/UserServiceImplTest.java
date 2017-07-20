package com.oocl.chatserver.service.impl;

import org.junit.Test;

import com.oocl.chatserver.service.UserService;

public class UserServiceImplTest {
	private UserService userService = new UserServiceImpl();
		
	@Test
	public void testRegister() {
		System.out.println(userService.register("Weir", "321"));
	}

	@Test
	public void testLogin() {
		System.out.println(userService.login("Weir", "3211"));
	}

}
