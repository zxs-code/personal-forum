package com.github.code.zxs.core.util;

import com.github.code.zxs.core.config.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

@Component
public class EmailUtils {

    private static Properties props = System.getProperties();

    private static EmailConfig emailConfig;

    @Autowired
    public void init(EmailConfig config) {
        emailConfig = config;
        props.put("mail.transport.protocol", "smtp"); // 使用的协议
        props.put("mail.smtp.host", emailConfig.getHost());
        props.put("mail.smtp.port", emailConfig.getPort());
        props.put("mail.smtp.starttls.enable", "true");// 使用 STARTTLS安全连接
        props.put("mail.smtp.auth", "true"); // 使用验证
        /**
         * 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启)
         * SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
         * 需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,QQ邮箱的SMTP(SLL)端口为465或587,
         * 其他邮箱自行去查看)
         */
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.socketFactory.port", emailConfig.getPort());
    }

    /**
     * 发送邮箱 <附件>
     *
     * @param receiveMail
     * @param title
     * @param file
     */
    public static void sendEmail(String receiveMail, String title, String content, String file) {
        try {
            //1，根据配置创建会话对象, 用于和邮件服务器交互
            Session session = Session.getInstance(props);
            //session.setDebug(true);   // 设置为debug模式, 可以查看详细的发送 log

            //2，创建一封邮件
            MimeMessage message = createMimeMessage(session, receiveMail, title, content, file);

            //3，根据 Session 获取邮件传输对象
            Transport transport = session.getTransport();

            // 4，使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
            transport.connect(emailConfig.getAccount(), emailConfig.getPassword());

            // 5，发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());

            // 6. 关闭连接
            transport.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建邮件信息<附件>
     *
     * @param session
     * @param receiveMail
     * @param title
     * @param file
     * @return
     * @throws Exception
     */
    private static MimeMessage createMimeMessage(Session session, String receiveMail, String title, String content, String file) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(emailConfig.getAccount(),emailConfig.getAccountName(), "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail));

        // 4. Subject: 邮件主题
        message.setSubject(title, "UTF-8");

        /*
         * 下面是邮件内容的创建:
         */
        MimeMultipart mm = new MimeMultipart();

        //1> 文本内容
        if (null != content) {
            MimeBodyPart text = new MimeBodyPart();
            text.setText(content);

            mm.addBodyPart(text);
        }

        //2> 附件
        if (null != file) {
            MimeBodyPart attachment = new MimeBodyPart();
            DataHandler dh = new DataHandler(new URL(file));
            //DataHandler dh = new DataHandler(new FileDataSource(file));
            attachment.setDataHandler(dh);
            attachment.setFileName(MimeUtility.encodeText(dh.getName()));

            mm.addBodyPart(attachment); // 如果有多个附件，可以创建多个多次添加
        }

        message.setContent(mm);

        // 设置发件时间
        message.setSentDate(new Date());

        // 保存上面的所有设置
        message.saveChanges();

        return message;
    }
}