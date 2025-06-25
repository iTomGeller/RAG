package com.cyberlanting.qwen_rag.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import com.cyberlanting.qwen_rag.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@EnableAsync
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

//    public void sendCaptchaEmail(String email, String authCode) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("872556642@qq.com");
//        message.setTo(email);
//        message.setSubject("Quan验证码");
//        message.setText("您的验证码为：" + authCode + "有效时间5分钟");
//        mailSender.send(message);
//    }


    public void sendCaptchaEmail(String email, String authCode) throws MessagingException {
        CompletableFuture.runAsync(() -> {
            try {
                processEmailSending(email, authCode);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Async
    private void processEmailSending(String email, String authCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Set sender, recipient, and subject
        helper.setFrom("872556642@qq.com");
        helper.setTo(email);
        helper.setSubject("Quan 验证服务");

        String htmlContent = buildHtmlishContent(authCode);
        helper.setText(htmlContent, true);

        addInkElements(helper);

        mailSender.send(message);
    }

    private String buildHtmlishContent(String authCode) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <title>验证函</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            color: #333;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            height: 100vh;\n" +
                "            background: linear-gradient(to right, #eef2f3, #d4e6f1);\n" +
                "        }\n" +
                "\n" +
                "        .letter {\n" +
                "            width: 80%;\n" +
                "            max-width: 500px;\n" +
                "            background-color: #ffffff;\n" +
                "            border: 1px solid #ddd;\n" +
                "            border-radius: 12px;\n" +
                "            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);\n" +
                "            padding-left: 80px;\n" +
                "            padding-bottom: 40px;\n" +
                "            padding-right: 80px;\n" +
                "            padding-top: 40px;\n" +
                "            position: relative;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "\n" +
                "        .letter::before {\n" +
                "            content: '';\n" +
                "            position: absolute;\n" +
                "            top: -10px;\n" +
                "            left: -10px;\n" +
                "            right: -10px;\n" +
                "            bottom: -10px;\n" +
                "            border: 1px dashed #ccc;\n" +
                "            z-index: -1;\n" +
                "            border-radius: 12px;\n" +
                "        }\n" +
                "\n" +
                "        .letter-header h1 {\n" +
                "            font-size: 32px;\n" +
                "            font-weight: bold;\n" +
                "            margin: 0;\n" +
                "            color: #2c3e50;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "\n" +
                "        .letter-body {\n" +
                "            font-size: 18px;\n" +
                "            line-height: 1.6;\n" +
                "            margin-top: 20px;\n" +
                "            color: #555;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "\n" +
                "        .code-container{\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .code {\n" +
                "            font-size: 30px;\n" +
                "            font-weight: bold;\n" +
                "            color: #2980b9;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 12px 20px;\n" +
                "            border: 2px solid #2980b9;\n" +
                "            border-radius: 8px;\n" +
                "            display: inline-block;\n" +
                "            background-color: #ecf7ff;\n" +
                "        }\n" +
                "\n" +
                "        .letter-footer {\n" +
                "            font-size: 14px;\n" +
                "            font-style: italic;\n" +
                "            color: #aaa;\n" +
                "            margin-top: 30px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .background-img {\n" +
                "            position: absolute;\n" +
                "            top: 0;\n" +
                "            left: 0;\n" +
                "            width: 100%;\n" +
                "            height: 100%;\n" +
                "            object-fit: cover;\n" +
                "            opacity: 0.05;\n" +
                "            z-index: 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"letter\">\n" +
                "        <div class=\"letter-header\">\n" +
                "            <h1>验证函</h1>\n" +
                "        </div>\n" +
                "        <div class=\"letter-body\">\n" +
                "            <p>您好，</p>\n" +
                "            <p>您正在我们的网站进行注册用户操作，我们向您的邮箱发送了一个验证码以确保是您本人在操作。</p>\n" +
                "            <p>您的验证码为：</p>\n" +
                "            <div class=\"code-container\">\n" +
                "                <div class=\"code\">" + authCode + "</div>\n" +
                "            </div>\n" +
                "            <p>请在5分钟内使用，以保证验证码的有效性。</p>\n" +
                "            <p>此邮件为系统自动发出，请勿回复本邮件。有任何问题，发送邮件至 872556642@qq.com，我们的客户服务团队会及时为您解答。</p>\n" +
                "        </div>\n" +
                "        <div class=\"letter-footer\">\n" +
                "            <p>© 2025 OtterAI. All Rights Reserved.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private void addInkElements(MimeMessageHelper helper) throws MessagingException {
        // 使用 ClassPathResource 加载资源目录下的图片
        ClassPathResource imageResource = new ClassPathResource("static/email_img/ink.jpg");
        if (imageResource.exists()) {
            helper.addInline("ink", imageResource);
        }
    }

}
