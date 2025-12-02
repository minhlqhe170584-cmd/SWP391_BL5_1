package dao; // Đổi từ dal -> dao

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.User; // Import class Users mới

public class UsersDAO extends DBContext { // Nhớ DBContext cũng phải nằm trong package dao hoặc import đúng

    // 1. Hàm Login
    public User login(String user, String pass) {
        String sql = "SELECT * FROM Users WHERE [user] = ? AND pass = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, user);
            st.setString(2, pass);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("uID"),
                        rs.getString("user"),
                        rs.getString("pass"),
                        rs.getInt("isSell"),
                        rs.getInt("isAdmin"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    // 2. Hàm Check User Tồn Tại
    public User checkAccountExist(String user) {
        String sql = "SELECT * FROM Users WHERE [user] = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, user);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("uID"),
                        rs.getString("user"),
                        rs.getString("pass"),
                        rs.getInt("isSell"),
                        rs.getInt("isAdmin"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    // 3. Hàm Đăng Ký
    public void signup(String user, String pass, String email) {
        String sql = "INSERT INTO Users([user], pass, isSell, isAdmin, email) VALUES(?,?,0,0,?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, user);
            st.setString(2, pass);
            st.setString(3, email);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}