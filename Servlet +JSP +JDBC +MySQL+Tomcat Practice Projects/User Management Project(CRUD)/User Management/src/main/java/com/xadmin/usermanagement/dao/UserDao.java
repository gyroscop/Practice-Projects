package com.xadmin.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xadmin.usermanagement.bean.User;

public class UserDao {

	private String jdbcURL = "jdbc:mysql://localhost:3306/userdb?userSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "3183";
	private String jdbcDriver = "com.mysql.jdbc.Driver";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users"+"  (name,email,country)"
	+ " VALUES (?, ?, ?);";
	
	private static final String SELECT_USERS_BY_ID = "SELECT id,name,email,country from users where id = ?;" ;
	private static final String SELECT_ALL_USERS = "SELECT * from users;" ;
	private static final String DELETE_USERS_SQL = "DELETE from users where id= ?;" ;
	private static final String UPDATE_USERS_SQL ="update users set name = ?,email= ?, country =? where id = ?;" ;
	
	public UserDao() {
		
	}
	///Get Database Connection
	protected Connection getConnection() {
		
		Connection connection =  null;
		
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return connection;
		
	}
	
	///Insert User
	public void insertUser(User user) throws SQLException {
		System.out.println(INSERT_USERS_SQL);
		try(Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)){
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate(); 
		}catch(SQLException e) {
			printSQLException(e);
		}
		
	}
	
	///select user by id
	public User SelectUser( int id ) throws SQLException {
		
		User user = null ;
		System.out.println(SELECT_USERS_BY_ID);
		try(Connection connection = getConnection(); 
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_BY_ID)){
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()){
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id,name,email,country) ;
				
			}
		}catch(SQLException e) {
			printSQLException(e); 
		}
		
		
		return user;
	
		
	}

/// Select all Users
	
	public List<User> SelectAllUser( )  {
		
		List<User> users = new ArrayList<>() ;
		
		System.out.println(SELECT_ALL_USERS);
		try(Connection connection = getConnection(); 
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)){
		
			System.out.println(preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()){
				int id =  rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id,name,email,country)) ;
				
			}
		}catch(SQLException e) {
			printSQLException(e); 
		}
		
		
		return users;
	
		
	}
	
///Update User
	
public boolean UpdateUser( User user ) throws SQLException {
		
		boolean rowUpdated   ;
		System.out.println(UPDATE_USERS_SQL);
		try(Connection connection = getConnection(); 
				PreparedStatement Statement = connection.prepareStatement(UPDATE_USERS_SQL)){
				System.out.println("Updated User : " + Statement);
				Statement.setString(1, user.getName());
				Statement.setString(2, user.getEmail());
				Statement.setString(3, user.getCountry());
				Statement.setInt(4, user.getId());
				
				rowUpdated = Statement.executeUpdate() > 0;
			
		}
		
		
	return rowUpdated ;
		
	}
	

public boolean DeleteUser(int id) throws SQLException {
	boolean userDeleted = false ;
	
	System.out.println(DELETE_USERS_SQL);
	try(Connection connection = getConnection(); 
			PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL)){
		
		preparedStatement.setInt(1, id);
		userDeleted = preparedStatement.executeUpdate() > 0;
	}
	
	return userDeleted;
}


private void printSQLException(SQLException ex) {
		for(Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState() );
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode() );
				System.err.println("Message: " + ((SQLException) e).getMessage());
				Throwable t = ex.getCause();
				while(t != null) {
					System.out.println("Cause : " + t);
					t = t.getCause();
				}
				
			}
		}
		
	}
	
	
}
