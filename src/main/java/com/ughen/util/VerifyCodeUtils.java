package com.ughen.util;

import com.ughen.constants.Constants;
import com.ughen.model.db.VerifyCode;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author feifei 生成验证码工具类
 */
public class VerifyCodeUtils extends CharacterUtil {

    private static Logger logger = LoggerFactory.getLogger(VerifyCodeUtils.class);


    /**
     * 生成指定长度的验证码
     */
    public static String verifyCode(int length) {
        String str = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char num = digits[random.nextInt(digits.length)];
            str += num;
        }
        return str;
    }

    /**
     * 生成的图片验证码
     */
    public static VerifyCode makeVerifyCode(HttpServletRequest request,
            HttpServletResponse response) {
        int width = 150;
        int height = 40;
        BufferedImage bfi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //获得Graphics对象就可以画图
        Graphics g = bfi.getGraphics();
        //1，设置背景（白框框）
        //白色的画笔
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        //设置字体的大写与粗
        g.setFont(new Font("a", Font.BOLD, 20));
        //2，具体生成随机数
        Random rom = new Random();
        char[] a = VerifyCodeUtils.digits;
        String str = "";
        //画出具体的图片
        for (int i = 0; i < 6; i++) {
            //生成的随机数
            char num = a[rom.nextInt(a.length)];
            str += num;
            //设置画笔的颜色（随机）
            g.setColor(new Color(rom.nextInt(256), rom.nextInt(256), rom.nextInt(256)));
            //画出线，x的位置每一之间增加20，y的坐标以20一条线，在线上或者是线下
            g.drawString("" + num, 20 * i, 20 + rom.nextInt(10));
            //PS：位置需要明确些，
        }
        //画出一些干扰线
        for (int i = 0; i < 10; i++) {
            //设置画笔的颜色（随机）
            g.setColor(new Color(rom.nextInt(256), rom.nextInt(256), rom.nextInt(256)));
            //位置也是随机，x，y的值不要超过矩形框框
            g.drawLine(rom.nextInt(100), rom.nextInt(40), rom.nextInt(100), rom.nextInt(40));
        }
        g.dispose();

        /**
         * 	返回图片验证码与随机数
         */
        VerifyCode vc = new VerifyCode();
        vc.setBfi(bfi);
        vc.setCode(str);
        return vc;
    }

    /**
     * 将生成的字符串存储在session中
     *
     * @param str code
     */
    public static void setCode(HttpServletRequest request, String str) {
        HttpSession session = request.getSession();
        session.setAttribute(Constants.VERIFY_SESSION_KEY, str);
    }

    /**
     * 流输出图片验证码
     */
    public static void outputStream(HttpServletResponse response, BufferedImage bfi) {
        ServletOutputStream sos = null;
        try {
            //禁止图像缓存
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            //创建二进制的输出流
            sos = response.getOutputStream();
            // 将图像输出到Servlet输出流中。
            ImageIO.write(bfi, "jpeg", sos);
            sos.flush();
        } catch (Exception e) {
            logger.error("VerifyCodeUtils | outputStream | 流异常" , e);
            e.printStackTrace();
        }finally {
            if(sos != null ){
                try {
                    sos.close();
                } catch (IOException e) {
                    logger.error("VerifyCodeUtils | outputStream | 关闭流异常" , e);
                    e.printStackTrace();
                }
            }
        }
    }


}
