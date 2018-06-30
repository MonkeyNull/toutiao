package com.nowcoder.util;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.velocity.VelocityProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeUtility;

import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

/**
 * Created by nowcoder on 2016/7/15. // course@nowcoder.com NKnk66
 */
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("小灰灰之家");
            InternetAddress from = new InternetAddress(nick + "<995373154@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            String result = VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();

        // 请输入自己的邮箱和密码，用于发送邮件
        mailSender.setUsername("xxxxx");
        //邮箱给的安全码代替密码！
        mailSender.setPassword("xxxxxx");
        mailSender.setHost("smtp.qq.com");
        // 请配置自己的邮箱和密码
        //邮箱的端口号，具体设置可以看邮箱
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put(" mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.starttls.enable",true);
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put(" mail.smtp.timeout ", "25000");
//        Properties prop = new Properties();
//
//        prop.put(" mail.smtp.auth ", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
//
//        prop.put("mail.smtp.starttls.enable", "true");
//
//        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //使用ssl协议来保证连接安全
//
//        prop.put(" mail.smtp.timeout ", "25000"); //传输超时时间



        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
