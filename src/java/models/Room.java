package models;

import java.sql.Timestamp;
import java.util.List;

public class Room {

    private int roomId;
    private String roomNumber;
    private int typeId;
    private String status;        // Map với cột 'room_status'
    private String roomPassword;
    private boolean isActiveLogin;
    private Timestamp lastCleanedAt;

    private boolean isEventRoom; // true = Sảnh tiệc, false = Phòng ngủ
    // Các trường phụ (Relation)
    private RoomType roomType;
    private List<Task> tasks;

    public Room() {
    }

    // Constructor 1: Đầy đủ (Dùng khi lấy từ DB)
    public Room(int roomId, String roomNumber, int typeId, String status, String roomPassword, boolean isActiveLogin, Timestamp lastCleanedAt) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.typeId = typeId;
        this.status = status;
        this.roomPassword = roomPassword;
        this.isActiveLogin = isActiveLogin;
        this.lastCleanedAt = lastCleanedAt;
    }

    // Constructor 2: Tương thích code cũ (Không có timestamp) - QUAN TRỌNG ĐỂ TRÁNH LỖI BIÊN DỊCH
//    public Room(int roomId, String roomNumber, int typeId, RoomType roomType, String status, String roomPassword, boolean isActiveLogin) {
//        this.roomId = roomId;
//        this.roomNumber = roomNumber;
//        this.typeId = typeId;
//        this.roomType = roomType;
//        this.status = status;
//        this.roomPassword = roomPassword;
//        this.isActiveLogin = isActiveLogin;
//    }
    public Room(int roomId, String roomNumber, int typeId, RoomType roomType, String status, String roomPassword, boolean isActiveLogin, boolean isEventRoom) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.typeId = typeId;
        this.roomType = roomType;
        this.status = status;
        this.roomPassword = roomPassword;
        this.isActiveLogin = isActiveLogin;
        this.isEventRoom = isEventRoom;
    }

    // --- GETTER & SETTER CHUẨN JAVA BEAN ---
    // JSP gọi ${r.activeLogin} sẽ tìm hàm này
    public boolean isActiveLogin() {
        return isActiveLogin;
    }

    // Servlet/DAO gọi hàm này
    public void setActiveLogin(boolean isActiveLogin) {
        this.isActiveLogin = isActiveLogin;
    }

    // --- Các Getter/Setter khác giữ nguyên ---
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

    public Timestamp getLastCleanedAt() {
        return lastCleanedAt;
    }

    public void setLastCleanedAt(Timestamp lastCleanedAt) {
        this.lastCleanedAt = lastCleanedAt;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean isIsActiveLogin() {
        return isActiveLogin;
    }

    public void setIsActiveLogin(boolean isActiveLogin) {
        this.isActiveLogin = isActiveLogin;
    }

    public boolean isIsEventRoom() {
        return isEventRoom;
    }

    public void setIsEventRoom(boolean isEventRoom) {
        this.isEventRoom = isEventRoom;
    }
    
    @Override
    public String toString() {
        return "Room{" + "roomId=" + roomId + ", roomNumber=" + roomNumber + ", status=" + status + '}';
    }
}
