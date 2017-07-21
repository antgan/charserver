package com.oocl.chatserver.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.oocl.chatserver.dao.UserDao;
import com.oocl.chatserver.pojo.User;
import com.oocl.chatserver.util.DBHelper;

public class UserDaoImpl implements UserDao {

	public UserDaoImpl() {}
	
	public int addUser(User user) {
		String sql = "insert into USERS(id, name, pwd) values(uidSeq.nextval,?,?)";
		Connection conn = DBHelper.getConnectionWithDs();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			DBHelper.setSqlValues(pst, user.getName(), user.getPwd());
			return pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.close(pst, null, conn);
		}
		return -1;
	}

	public int deleteUser(Integer id) {
		String sql = "delete USERS where id = ? ";
		Connection conn = DBHelper.getConnectionWithDs();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			DBHelper.setSqlValues(pst, id);
			return pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.close(pst, null, conn);
		}
		return -1;
	}

	public int updateUser(User user) {
		String sql = "update USERS set name = ?, pwd = ? where id = ? ";
		Connection conn = DBHelper.getConnectionWithDs();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			DBHelper.setSqlValues(pst, user.getName(), user.getPwd(), user.getId());
			return pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.close(pst, null, conn);
		}
		return -1;
	}

	public List<User> queryAllUser() {
		Connection conn = DBHelper.getConnection();
		List<User> list = new ArrayList<User>();
		User user = null;
		String sql = "select * from users";
		PreparedStatement pst = null;
		ResultSet rst = null;
		try {
			pst =  conn.prepareStatement(sql);
			rst = pst.executeQuery();
		
			while(rst.next()){
				user = new User();
				user.setId(rst.getInt(1));
				user.setName(rst.getString(2));
				user.setPwd(rst.getString(3));
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.close(pst, rst, conn);
		}
		return list;
	}

	public User queryUserById(Integer id) {
		Connection conn = DBHelper.getConnectionWithDs();
		User user = null;
		String sql = "select * from users where id = ? ";
		PreparedStatement pst = null;
		ResultSet rst = null;
		try {
			pst =  conn.prepareStatement(sql);
			pst.setInt(1, id);
			rst = pst.executeQuery();
			while(rst.next()){
				user = new User();
				user.setId(rst.getInt(1));
				user.setName(rst.getString(2));
				user.setPwd(rst.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.close(pst, rst, conn);
		}
		return user;
	}
	
	public User queryUserByNameAndPwd(String name, String pwd) {
		Connection conn = DBHelper.getConnectionWithDs();
		User user = null;
		String sql = "select * from users where name = ? and pwd = ? ";
		PreparedStatement pst = null;
		ResultSet rst = null;
		try {
			pst =  conn.prepareStatement(sql);
			pst.setString(1, name);
			pst.setString(2, pwd);
			rst = pst.executeQuery();
			while(rst.next()){
				user = new User();
				user.setId(rst.getInt(1));
				user.setName(rst.getString(2));
				user.setPwd(rst.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.close(pst, rst, conn);
		}
		return user;
	}
	
	public boolean isExist(String name) {
		Connection conn = DBHelper.getConnectionWithDs();
		String sql = "select * from users where name = ? ";
		PreparedStatement pst = null;
		ResultSet rst = null;
		try {
			pst =  conn.prepareStatement(sql);
			pst.setString(1, name);
			rst = pst.executeQuery();
			int counter = 0;
			while(rst.next()){
				counter++;
			}
			if(counter == 1){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.close(pst, rst, conn);
		}
		return false;
	}

}
