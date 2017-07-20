package com.oocl.chatserver.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.oocl.chatserver.http.request.HttpRequest;


/**
 * 
 * @author HEZE2
 *
 */
public class ParserUtil {
	
	public static HttpRequest parse(InputStream in)  {
		HttpRequest request=new HttpRequest();
		try {
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			String line =reader.readLine();
			String[] lineSplit = line.split(" ");
			String method = lineSplit[0];
			String pathAndParam = lineSplit[1];
			String[] ppSplit = pathAndParam.split("\\?");
			String path = null;
			String param = null;
			Map<String,String> params = new HashMap<String,String>();
			//如果没有?
			if(ppSplit.length == 1){
				path = ppSplit[0];
			}else{
				path = ppSplit[0];
				param = ppSplit[1];
				String[] pps = param.split("=");
				params.put(pps[0], pps[1]);
			}	
			request.setMethod(method);
			request.setPath(path);
			request.setParams(params);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return request;
	}

	
	public static void main(String[] args) {
		HttpRequest r = ParserUtil.parse(System.in);
		
		System.out.println(r);
	}
	
	
}
