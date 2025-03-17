package com.project.datn.service.impl;

import com.project.datn.service.IEmailTemplateService;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
public class IEmailTemplateServiceImpl implements IEmailTemplateService {
    @Override
    public void sendMaXacNhanToEmail(Integer mailType) {

        final String username = "buidaithang16122003@gmail.com";
        final String password = "mvhlkegmrghvsonn"; // mật khẩu ứng dụng

        // Cài đặt thông tin host và port cho Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            Message message = new MimeMessage(session);

            // Đặt người gửi
            message.setFrom(new InternetAddress(username));

            // Đặt người nhận
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(username));

            // Đặt tiêu đề
            message.setSubject("Xác nhận đăng ký tài khoản");

            // Đặt nội dung email
            message.setText("Mã xác nhận của bạn là: " + mailType);

            // Gửi email
            Transport.send(message);

            System.out.println("Email đã được gửi thành công!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
