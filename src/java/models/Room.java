/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author My Lap
 */
public class Room {
    // Thuộc tính ánh xạ từ bảng [Rooms]
    private int roomId;             // room_id
    private String roomNumber;      // room_number
    private int typeId;             // type_id (Khóa ngoại)
    private RoomStatus status;      // ĐÃ THAY ĐỔI: Sử dụng Enum RoomStatus
    private String roomPassword;    // room_password
    private boolean isActiveLogin;  // is_active_login

    // Thuộc tính nhúng (Composition): Chi tiết Loại phòng
    private RoomType roomTypeDetail;

    public Room() {
    }

    // CẬP NHẬT CONSTRUCTOR: Tham số 'status' là RoomStatus
    public Room(int roomId, String roomNumber, int typeId, RoomStatus status, String roomPassword, boolean isActiveLogin, RoomType roomTypeDetail) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.typeId = typeId;
        this.status = status;
        this.roomPassword = roomPassword;
        this.isActiveLogin = isActiveLogin;
        this.roomTypeDetail = roomTypeDetail;
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

    // CẬP NHẬT GETTER: Trả về RoomStatus
    public RoomStatus getStatus() {
        return status;
    }

    // CẬP NHẬT SETTER: Nhận vào RoomStatus
    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    // ĐÃ SỬA: Đổi tên theo Java Bean Convention (isPropertyName)
    public boolean isActiveLogin() {
        return isActiveLogin;
    }

    // CẬP NHẬT SETTER: Tên phương thức đã sửa
    public void setIsActiveLogin(boolean isActiveLogin) {
        this.isActiveLogin = isActiveLogin;
    }

    public RoomType getRoomTypeDetail() {
        return roomTypeDetail;
    }

    public void setRoomTypeDetail(RoomType roomTypeDetail) {
        this.roomTypeDetail = roomTypeDetail;
    }
}