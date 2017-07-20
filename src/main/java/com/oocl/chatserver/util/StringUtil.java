package com.oocl.chatserver.util;

import java.util.UUID;

public class StringUtil {

	
	public static String createToken(){
		return UUID.randomUUID().toString();
	}
}
