package dbContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {
            DBContext db = new DBContext();
            if (db.connection != null) {
                System.out.println("Kết nối thành công!");
            } else {
                System.out.println("Kết nối thất bại!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e);
        }
    }
}