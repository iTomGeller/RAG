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
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>验证函</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: '楷体', serif;\n" +
                "            font-weight: 700;\n" +
                "            color: #333;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            height: 100vh;\n" +
                "        }\n" +
                "       .letter {\n" +
                "            width: 80%;\n" +
                "            max-width: 800px;\n" +
                "            background-color: rgba(255, 255, 255, 0.8);\n" +
                "            border: 2px solid #999;\n" +
                "            border-radius: 8px;\n" +
                "            padding: 40px;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "            position: relative;\n" +
                "        }\n" +
                "       .letter::before {\n" +
                "            content: '';\n" +
                "            position: absolute;\n" +
                "            top: -10px;\n" +
                "            left: -10px;\n" +
                "            right: -10px;\n" +
                "            bottom: -10px;\n" +
                "            border: 1px dashed #999;\n" +
                "            z-index: -1;\n" +
                "        }\n" +
                "       .letter-header {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 30px;\n" +
                "        }\n" +
                "       .letter-header h1 {\n" +
                "            font-size: 36px;\n" +
                "            font-weight: bold;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "       .letter-body {\n" +
                "            line-height: 1.8;\n" +
                "            font-size: 20px;\n" +
                "            text-align: justify;\n" +
                "        }\n" +
                "       .code {\n" +
                "            font-size: 30px;\n" +
                "            font-weight: bold;\n" +
                "            color: #2c3e50;\n" +
                "            margin: 20px 0 20px 4em; /* 添加 margin-left 实现缩进 */\n" +
                "            text-align: center;\n" +
                "            padding: 10px;\n" +
                "            border: 2px solid #2c3e50;\n" +
                "            border-radius: 8px;\n" +
                "            display: inline-block;\n" +
                "            background-color: rgba(255, 255, 255, 0.5);\n" +
                "        }\n" +
                "       .letter-footer {\n" +
                "            font-size: 16px;\n" +
                "            font-style: italic;\n" +
                "            text-align: center;\n" +
                "            margin-top: 40px;\n" +
                "        }\n" +
                "       .background-img {\n" +
                "            position: absolute;\n" +
                "            top: 0;\n" +
                "            left: 0;\n" +
                "            width: 100%;\n" +
                "            height: 100%;\n" +
                "            object-fit: cover;\n" +
                "            z-index: -1;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"letter\">\n" +
                "        <img class=\"background-img\" src=\"cid:ink\" alt=\"背景图片\">\n" +
                "        <div class=\"letter-header\">\n" +
                "            <h1>验证函</h1>\n" +
                "        </div>\n" +
                "        <div class=\"letter-body\">\n" +
                "            <p>阁下台鉴：</p>\n" +
                "            <p>&emsp;&emsp;您的验证码为：</p>\n" +
                "            <div class=\"code\">" + authCode + "</div>\n" +
                "            <p>&emsp;&emsp;有效期5分钟，请及时研磨书写。</p>\n" +
                "        </div>\n" +
                "        <div class=\"letter-footer\">\n" +
                "            <p>© 赛博兰亭团队</p>\n" +
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
