<%-- 
    Document   : contact
    Description: Trang Liên Hệ (Đã bỏ Form, chỉ hiện thông tin)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zxx">

<head>
    <title>Liên Hệ | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
</head>

<body>
    <div id="preloder">
        <div class="loader"></div>
    </div>

    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <section class="contact-section spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 offset-lg-2">
                    <div class="contact-text text-center">
                        <h2>Thông Tin Liên Hệ</h2>
                        <p>Chúng tôi luôn sẵn sàng lắng nghe và hỗ trợ bạn 24/7. Hãy liên hệ với chúng tôi qua các kênh dưới đây để được giải đáp thắc mắc và hỗ trợ đặt phòng nhanh nhất.</p>
                        
                        <div class="d-flex justify-content-center">
                            <table style="max-width: 500px; text-align: left;">
                                <tbody>
                                    <tr>
                                        <td class="c-o" style="width: 100px; font-weight: bold; color: #e67e22;">Địa chỉ:</td>
                                        <td>Khu Công nghệ cao Hòa Lạc, Thạch Thất, Hà Nội</td>
                                    </tr>
                                    <tr>
                                        <td class="c-o" style="font-weight: bold; color: #e67e22;">Điện thoại:</td>
                                        <td>(024) 6688 8888</td>
                                    </tr>
                                    <tr>
                                        <td class="c-o" style="font-weight: bold; color: #e67e22;">Email:</td>
                                        <td>info@smarthotel.com</td>
                                    </tr>
                                    <tr>
                                        <td class="c-o" style="font-weight: bold; color: #e67e22;">Fax:</td>
                                        <td>+(024) 6688 9999</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
            </div>
            
            <div class="map">
                <iframe
                    src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3724.485536643666!2d105.52495631476326!3d21.01324898600674!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31345b465a4e65fb%3A0xaae6040cfabe8fe!2zVHLGsOG7nW5nIMSQ4bqhaSBI4buNYyBGUFQ!5e0!3m2!1svi!2s!4v1678852378411!5m2!1svi!2s"
                    height="470" style="border:0;" allowfullscreen="" loading="lazy"></iframe>
            </div>
        </div>
    </section>
    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>

</html>