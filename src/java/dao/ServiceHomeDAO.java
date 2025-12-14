package dao;

import dbContext.DBContext;
import models.ServiceHome;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceHomeDAO extends DBContext {

    // 1. Đếm tổng số (Giữ nguyên, chỉ check lại tên bảng)
    public int countTotalServices() {
        String sql = "SELECT COUNT(*) FROM ServiceCategories";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. Lấy danh sách phân trang
    public List<ServiceHome> getServicesByPage(int pageIndex, int pageSize) {
        List<ServiceHome> list = new ArrayList<>();
        
        // SỬA LẠI: ORDER BY category_id (Không phải service_id)
        String sql = "SELECT * FROM ServiceCategories "
                   + "ORDER BY category_id ASC " 
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, (pageIndex - 1) * pageSize); 
            st.setInt(2, pageSize);                   
            
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ServiceHome s = new ServiceHome();
                
                // === QUAN TRỌNG: SỬA TÊN CỘT CHO KHỚP DB ===
                s.setCategoryId(rs.getInt("category_id"));     // Cũ: service_id -> Sai
                s.setCategoryName(rs.getString("category_name")); // Cũ: service_name -> Sai
                
                // Các cột này thường đúng rồi (kiểm tra lại DB của bạn nếu cần)
                s.setDescription(rs.getString("description"));
                s.setImageUrl(rs.getString("image_url"));
                s.setIconClass(rs.getString("icon_class"));
                s.setLinkUrl(rs.getString("link_url"));
                s.setBtnText(rs.getString("btn_text"));
                
                list.add(s);
            }
        } catch (Exception e) {
            // Đây là nơi bắt lỗi. Nếu sai tên cột, nó sẽ in lỗi đỏ lòm ở cửa sổ Output NetBeans
            e.printStackTrace(); 
        }
        return list;
    }
}