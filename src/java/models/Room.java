/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */
public class Room {
    private int roomId;
    private String roomNumber;
    private int typeId;
    private String status;       // Available, Occupied, Dirty...
    private String roomPassword; // Mật khẩu đăng nhập tạm thời
    private boolean isActiveLogin; // true: Đang cho phép đăng nhập

    public Room() {
    }

    public Room(int roomId, String roomNumber, int typeId, String status, String roomPassword, boolean isActiveLogin) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.typeId = typeId;
        this.status = status;
        this.roomPassword = roomPassword;
        this.isActiveLogin = isActiveLogin;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    public boolean isActiveLogin() {
        return isActiveLogin;
    }

    public void setActiveLogin(boolean activeLogin) {
        isActiveLogin = activeLogin;
    }

    @Override
    public String toString() {
        return "Room{" + "roomId=" + roomId + ", roomNumber=" + roomNumber + ", status=" + status + '}';
    }
}