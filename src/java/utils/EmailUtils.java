package utils;

import java.util.Properties;
import java.util.Random;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtils {
    
    // Hàm sinh mã OTP ngẫu nhiên 6 số
    public static String generateOTP() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    // Hàm gửi Email (Trả về boolean: true=thành công, false=thất bại)
    public static boolean sendEmail(String to, String otp) {
        // 1. Cấu hình Email người gửi
        final String from = "ruapheco@gmail.com"; 
        final String password = "jtzb wofv ifbd ivvt"; 

        // 2. Cấu hình Server Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // 3. Tạo phiên gửi mail
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };
        
        Session session = Session.getInstance(props, auth);

        // 4. Soạn và Gửi tin nhắn
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("Mã xác nhận quên mật khẩu - Smart Hotel");
            msg.setText("Mã OTP của bạn là: " + otp + "\n\nVui lòng không chia sẻ mã này.", "UTF-8");

            Transport.send(msg);
            System.out.println("Gửi mail thành công!");
            return true; // Trả về TRUE nếu gửi được
            
        } catch (Exception e) {
            System.out.println("Gửi mail thất bại: " + e.getMessage());
            e.printStackTrace();
            return false; // Trả về FALSE nếu lỗi
        }
    }
    
    // Hàm main để test nhanh
    public static void main(String[] args) {
        String testEmail = "ruapheco@gmail.com"; 
        boolean result = sendEmail(testEmail, "123456");
        System.out.println("Kết quả gửi: " + result);
    }
}