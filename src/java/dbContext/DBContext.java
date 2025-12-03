package dbContext;

import dao.RoomDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Room;

/*
 * Lưu ý: Cần thêm thư viện mssql-jdbc.jar vào phần Libraries của Project thì mới chạy được.
 */
public class DBContext {

    protected Connection connection;

    public DBContext() {
        try {
            // 1. Thông tin đăng nhập
            String user = "sa";
            String pass = "123";

            // 2. Tên Database (Sửa lại tên này nếu DB của bạn tên khác)
            String dbName = "SmartHotelDB";

            // 3. Cấu hình kết nối
            // encrypt=true;trustServerCertificate=true: Để tránh lỗi bảo mật SSL trên Java mới
            String url = "jdbc:sqlserver://localhost:1433;databaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";

            // 4. Gọi Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 5. Mở kết nối
            connection = DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Hàm main này để chạy thử xem kết nối được chưa (Test Connection)
    // Cách dùng: Chuột phải vào file này -> Run File (Shift + F6)
    public static void main(String[] args) {
        System.out.println("--- BẮT ĐẦU KIỂM TRA KẾT NỐI VÀ DAO ---");

        // 1. Kiểm tra kết nối DBContext
        DBContext db = new DBContext();
        if (db.connection != null) {
            System.out.println("Kết nối DBContext thành công!");
        } else {
            System.out.println("Kết nối DBContext thất bại! Kiểm tra console log chi tiết.");
            return; // Dừng lại nếu kết nối thất bại
        }

        // 2. Nếu kết nối thành công, thử gọi RoomDAO
        System.out.println("\n--- THỬ GỌI RoomDAO.getAllRooms() ---");
        RoomDAO roomDAO = new RoomDAO();
        try {
            List<Room> roomList = roomDAO.getAllRooms();

            if (roomList != null && !roomList.isEmpty()) {
                System.out.println("Truy vấn RoomDAO thành công. Tìm thấy " + roomList.size() + " phòng.");

                // In ra chi tiết 2 phòng đầu tiên để kiểm tra ánh xạ
                for (int i = 0; i < Math.min(2, roomList.size()); i++) {
                    Room room = roomList.get(i);
                    System.out.println("  - Phòng " + (i + 1) + ": #" + room.getRoomNumber()
                            + " | Loại: " + room.getRoomTypeDetail().getTypeName()
                            + " | Trạng thái: " + room.getStatus());
                }
            } else {
                System.out.println("Truy vấn thành công, nhưng không tìm thấy phòng nào trong DB.");
            }
        } catch (Exception e) {
            System.err.println("LỖI KHI GỌI RoomDAO: " + e.getMessage());
        }

        System.out.println("--- KẾT THÚC KIỂM TRA ---");
    }

    public Connection getConnection() {
        return connection;
    }
}
