package com.funworld.games;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {
	String username;
	String password;
	String name;
	int tokens;
	public User(String u, String p) {
		username = u;
		password = p;
	}
	public String verify() {
		try {
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://mdakram28-pc:5432/funWorld","postgres", "admin");
			
			Statement st = conn.createStatement();
			String q = "SELECT * FROM users WHERE username = '"+username+"' AND password = '"+password+"'";
			System.out.println(q);
			ResultSet rs = st.executeQuery(q);
			
			int i = 0;
			String name;
			int tokens = 0;
			while(rs.next()) {
			    i++;
				name = rs.getString("name");
				tokens = rs.getInt("tokens");
			}
			if(i==0){
				return "User not found";
			}
			rs.close();
			st.close();
			
			if(tokens < 100){
				return "You have less than 100 tokens";
			}else{
				return "";
			}

		} catch (Exception e2) {
			System.out.println(e2);
			return e2.getMessage();
		}
	}
}
