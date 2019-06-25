package com.ughen.util.email;

/**
 * Author:@Yonghong Zhang
 * Date: 17:34 2019/1/22
 */

import com.sun.mail.util.MailSSLSocketFactory;
import com.ughen.config.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Component
public class SendMailUtil {


    private static EmailConfig emailConfig;


    @Autowired
    public SendMailUtil(EmailConfig emailConfig) {
        SendMailUtil.emailConfig = emailConfig;
    }

    /**
     * 邮件单发（自由编辑短信，并发送，适用于私信）
     *
     * @param toEmailAddress 收件箱地址
     * @param emailTitle     邮件主题
     * @param emailContent   邮件内容
     * @throws Exception
     */
    public static void sendEmail(String toEmailAddress, String emailTitle, String emailContent) throws Exception {
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 端口号
        props.put("mail.smtp.port", 465);
        // 设置邮件服务器主机名
        props.setProperty("mail.smtp.host", emailConfig.getHost());
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        /**
         * SSL认证，注意腾讯邮箱是基于SSL加密的，所以需要开启才可以使用
         */
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        //设置是否使用ssl安全连接（一般都使用）
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        //创建会话
        Session session = Session.getInstance(props);
        //获取邮件对象
        //发送的消息，基于观察者模式进行设计的
        Message msg = new MimeMessage(session);
        //设置邮件标题
        msg.setSubject(emailTitle);
        //设置邮件内容
        //使用StringBuilder，因为StringBuilder加载速度会比String快，而且线程安全性也不错
        StringBuilder builder = new StringBuilder();
        //写入内容
        builder.append("\n" + emailContent);
        //定义要输出日期字符串的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //在内容后加入邮件发送的时间
        builder.append("\n时间：" + sdf.format(new Date()));
        //设置显示的发件时间
        msg.setSentDate(new Date());
        //设置邮件内容
        msg.setText(builder.toString());
        //设置发件人邮箱
        // InternetAddress 的三个参数分别为: 发件人邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
        msg.setFrom(new InternetAddress(emailConfig.getFrom(), emailConfig.getName(), "UTF-8"));
        //得到邮差对象
        Transport transport = session.getTransport();
        //连接自己的邮箱账户
        //密码不是自己QQ邮箱的密码，而是在开启SMTP服务时所获取到的授权码
        //connect(host, user, password)
        transport.connect(emailConfig.getHost(), emailConfig.getFrom(), emailConfig.getPassword());
        //发送邮件
        transport.sendMessage(msg, new Address[]{new InternetAddress(toEmailAddress)});
        //将该邮件保存到本地
//        OutputStream out = new FileOutputStream(System.currentTimeMillis() + "-" + toEmailAddress + "-" + emailTitle + ".eml");
//        msg.writeTo(out);
//        out.flush();
//        out.close();
        transport.close();
    }

    /**
     * 发送html邮件
     *
     * @param toEmailAddress 收件箱地址
     * @param emailTitle     邮件主题
     * @param emailContent   邮件内容
     * @throws Exception
     */
    public static void sendHtmlMail(String toEmailAddress, String emailTitle, String emailContent) throws Exception {
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 端口号
        props.put("mail.smtp.port", 465);
        // 设置邮件服务器主机名
        props.setProperty("mail.smtp.host", emailConfig.getHost());
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        /**
         * SSL认证，注意腾讯邮箱是基于SSL加密的，所以需要开启才可以使用
         */
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        //设置是否使用ssl安全连接（一般都使用）
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        //创建会话
        Session session = Session.getInstance(props);
        //获取邮件对象
        //发送的消息，基于观察者模式进行设计的
        Message msg = new MimeMessage(session);
        //设置邮件标题
        msg.setSubject(emailTitle);
        //设置邮件内容
        msg.setSentDate(new Date());
        Multipart mainPart = new MimeMultipart();

        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart html = new MimeBodyPart();
        // 设置HTML内容
        html.setContent(emailContent, "text/html; charset=utf-8");
        mainPart.addBodyPart(html);
        msg.setContent(mainPart);
        //设置发件人邮箱
        // InternetAddress 的三个参数分别为: 发件人邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
        msg.setFrom(new InternetAddress(emailConfig.getFrom(), emailConfig.getName(), "UTF-8"));
        //得到邮差对象
        Transport transport = session.getTransport();
        //连接自己的邮箱账户
        //密码不是自己QQ邮箱的密码，而是在开启SMTP服务时所获取到的授权码
        //connect(host, user, password)
        transport.connect(emailConfig.getHost(), emailConfig.getFrom(), emailConfig.getPassword());
        //发送邮件
        transport.sendMessage(msg, new Address[]{new InternetAddress(toEmailAddress)});
        //将该邮件保存到本地
//        OutputStream out = new FileOutputStream(System.currentTimeMillis() + "-" + toEmailAddress + "-" + emailTitle + ".eml");
//        msg.writeTo(out);
//        out.flush();
//        out.close();
        transport.close();
    }

}
