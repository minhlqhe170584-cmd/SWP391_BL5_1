/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dbContext.DBContext;
import models.Role;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class RoleDAO extends DBContext {
    private static final String GET_ALL_ROLES = "SELECT role_id, role_name FROM StaffRoles ORDER BY role_id";
    private static final String GET_ROLE_BY_ID = "SELECT role_id, role_name FROM StaffRoles WHERE role_id = ? ";
    private static final String INSERT_ROLE = "INSERT INTO StaffRoles (role_name) VALUE (?)";
    private static final String UPDATE_ROLE = "UPDATE StaffRoles SET role_name = ? WHERE role_id = ?";
    private static final String DELETE_ROLE = "DELETE FROM StaffRoles WHERE role_id = ? ";
    
    public RoleDAO(){
        super();
    }
    public Role getRoleById(int id) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        Role role = null;
        try {
            ps = connection.prepareStatement(GET_ROLE_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
            }          
        }finally {
            try{
                if(rs != null) rs.close();
                if(ps != null) ps.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return role;
    }
    public boolean createRole(String roleName) throws SQLException{
        PreparedStatement ps = null;
        try{
            ps = connection.prepareStatement(INSERT_ROLE);
            ps.setString(1, roleName);
            return ps.executeUpdate()>0;
        } finally {
            if(ps != null ) ps.close();
        }
    }
    
    public List<Role> getAllRoles(){
        List<Role> list  = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(GET_ALL_ROLES);
            rs = ps.executeQuery();
            
            while(rs.next()){
                list.add(new Role(rs.getInt("role_id"),rs.getString("role_name")));
            }   
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(rs != null) rs.close();
                if(ps != null) ps.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return list;
    }
    
    public boolean updateRole(int roleId, String newRoleName) throws SQLException{
        PreparedStatement ps = null;
        try{
            ps = connection.prepareStatement(UPDATE_ROLE);
            ps.setString(1, newRoleName);
            ps.setInt(2, roleId);
            return ps.executeUpdate() > 0;
        }finally {
            if(ps != null) ps.close();
        }
    }
    
    public boolean deleteRole(int roleId) throws SQLException{
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(DELETE_ROLE);
            ps.setInt(1, roleId);
            return ps.executeUpdate() > 0;
        }finally {
            if (ps != null )ps.close();
        }
    }
}
