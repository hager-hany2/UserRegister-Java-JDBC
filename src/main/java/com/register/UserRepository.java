package com.register;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserRepository {

    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "username VARCHAR(50), " +
                     "email VARCHAR(50), " +
                     "password VARCHAR(255))";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(User user) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
            System.out.println("User registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JSONArray getAllUsers() {
        JSONArray users = new JSONArray();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JSONObject user = new JSONObject();
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                user.put("email", rs.getString("email"));
                users.put(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    public JSONObject getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JSONObject user = new JSONObject();
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                user.put("email", rs.getString("email"));
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }
    public boolean update(int id, String username, String email) {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        boolean first = true;
        if (username != null) { sql.append("username=?"); first=false; }
        if (email != null) { if(!first) sql.append(", "); sql.append("email=?"); }
        sql.append(" WHERE id=?");

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (username != null) stmt.setString(index++, username);
            if (email != null) stmt.setString(index++, email);
            stmt.setInt(index, id);

            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            return deleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }}
    	
    
}

