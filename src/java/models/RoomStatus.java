/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author My Lap
 */
public enum RoomStatus {
    AVAILABLE("Available"),     // Phòng sẵn sàng cho thuê
    OCCUPIED("Occupied"),       // Phòng đang có khách
    CLEANING("Cleaning"),       // Phòng đang được dọn dẹp
    OUT_OF_SERVICE("Out Of Service"); // Phòng tạm ngưng hoạt động (sửa chữa, bảo trì)

    private final String displayValue;

    // Constructor cho Enum
    private RoomStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    // Phương thức trả về giá trị chuỗi sẽ lưu trong database/hiển thị
    public String getDisplayValue() {
        return displayValue;
    }
    
    // Phương thức tĩnh để chuyển từ giá trị chuỗi trong DB thành Enum
    public static RoomStatus fromString(String text) {
        for (RoomStatus status : RoomStatus.values()) {
            if (status.displayValue.equalsIgnoreCase(text)) {
                return status;
            }
        }
        // Trả về một giá trị mặc định hoặc throw exception nếu không tìm thấy
        return null; 
    }
}