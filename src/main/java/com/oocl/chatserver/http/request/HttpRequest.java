package com.oocl.chatserver.http.request;

import java.util.Map;

public class HttpRequest{
	private String method;
	private String path;
	private Map<String,String> params;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return "HttpRequest [method=" + method + ", path=" + path + ", params="
				+ params + "]";
	}
	
	
	
}
