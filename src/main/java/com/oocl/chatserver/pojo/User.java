package com.oocl.chatserver.pojo;

/**
 * User
 * @author GANAB
 *
 */
public class User {

	private Integer id;
	private String name;
	private String pwd;
	
	public User() {}
	
	public User(String name, String pwd) {
		super();
		this.name = name;
		this.pwd = pwd;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
	
}
