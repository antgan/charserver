package com.oocl.chatserver.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StringUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String createToken(){
		return UUID.randomUUID().toString();
	}
	
	public static String getFormatDateStr(long time){
		return sdf.format(new Date(time));
	}

}
