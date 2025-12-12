package utils;

import java.util.Properties;
import java.util.Random;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtils {

    // Cấu hình Email (Lấy từ code cũ của bạn)
    private static final String FROM_EMAIL = "ruapheco@gmail.com";
    private static final String EMAIL_PASSWORD = "jtzb wofv ifbd ivvt";

    /**
     * 1. Hàm sinh mã ngẫu nhiên (Dùng cho cả OTP số hoặc Mã vé chữ+số)
     * @param length Độ dài mã mong muốn
     * @return Chuỗi mã ngẫu nhiên
     */
    public static String generateRandomCode(int length) {
        // Bao gồm cả chữ và số để làm mật khẩu/mã vé
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 2. Hàm gửi Email ĐA DỤNG (Generic)
     * Dùng được cho cả Booking, OTP, Thông báo...
     * @param toEmail Email người nhận
     * @param subject Tiêu đề mail
     * @param body Nội dung mail
     * @return true nếu gửi thành công
     */
    public static boolean sendEmail(String toEmail, String subject, String body) {
        // Cấu hình Server Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Tạo phiên làm việc (Session)
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        };
        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(new InternetAddress(FROM_EMAIL));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject(subject);
            msg.setText(body, "UTF-8"); // Hỗ trợ Tiếng Việt

            Transport.send(msg);
            System.out.println("Gửi mail thành công tới: " + toEmail);
            return true;
        } catch (MessagingException e) {
            System.out.println("Gửi mail thất bại: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 3. (Giữ lại để tương thích code cũ) Hàm gửi OTP chuyên biệt
     * Hàm này gọi lại hàm sendEmail ở trên cho gọn code
     */
    public static boolean sendEmailOTP(String to, String otp) {
        String subject = "Mã xác nhận quên mật khẩu - Smart Hotel";
        String content = "Mã OTP của bạn là: " + otp + "\n\nVui lòng không chia sẻ mã này.";
        // Gọi hàm chung ở trên
        return sendEmail(to, subject, content); 
    }

    // Hàm Main để test nhanh
    public static void main(String[] args) {
        // Test thử sinh mã
        String code = generateRandomCode(8);
        System.out.println("Mã random: " + code);
        
        // Test gửi mail (Bỏ comment để chạy thật)
        // sendEmail("email_cua_ban@gmail.com", "Test Smart Hotel", "Mã đặt phòng của bạn là: " + code);
    }
}