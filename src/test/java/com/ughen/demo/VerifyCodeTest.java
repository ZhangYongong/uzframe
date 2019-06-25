package com.ughen.demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
import org.junit.Test;

public class VerifyCodeTest {
		@Test
		public void Test() throws Exception{
            final Base64.Decoder decoder = Base64.getDecoder();
            final Base64.Encoder encoder = Base64.getEncoder();


                final String text = "{\n" + "\t\"mobile\":\"15190411498\",\n"
                        + "\"platform\":\"uzTest\",\n" + "\"bindThird\":\"no\",\n"
                        + "\"password\":\"123456\",\n"
                        + "\t\"deviceNo\":\"third-app-andriod125896477\",\n"
                        + "\t\"code\":\"65acf96717b0b7dd62d0bfaa7e60ac58\"\n" + "\t\n" + "}";
                final byte[] textByte = text.getBytes("UTF-8");
                //编码
                final String encodedText = encoder.encodeToString(textByte);
                System.out.println(encodedText);
//解码
                System.out.println(new String(decoder.decode(encodedText), "UTF-8"));



		}

		@Test
		public void Test2() throws Exception{
				int width=100;
				int height=40;
				BufferedImage bfi =new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g=bfi.getGraphics();//获得Graphics对象就可以画图
				//1，设置背景（白框框）
				g.setColor(Color.WHITE);//白色的画笔
				g.fillRect(0, 0, width, height);
				//2，具体生成随机数
				String str="";//保存数据
				Random rom=new Random();
				//设置字体的大写与粗
				g.setFont(new Font("a", Font.BOLD,20));
				//画出具体的图片

				for(int i=0;i<4;i++){
						int num=rom.nextInt(10);//生成的随机数
						g.setColor(new Color(rom.nextInt(256),rom.nextInt(256), rom.nextInt(256)));//设置画笔的颜色（随机）
						g.drawString(""+num, 20*i, 20+rom.nextInt(10));//画出线，x的位置每一之间增加20，y的坐标以20一条线，在线上或者是线下
						//PS：位置需要明确些，
				}
				//画出一些干扰线
				for (int i = 0; i < 10; i++) {
						g.setColor(new Color(rom.nextInt(256),rom.nextInt(256), rom.nextInt(256)));//设置画笔的颜色（随机）
						g.drawLine(rom.nextInt(100),rom.nextInt(40), rom.nextInt(100), rom.nextInt(40));//位置也是随机，x，y的值不要超过矩形框框
				}
				g.dispose();



				ImageIO.write(bfi, "JPEG", new FileOutputStream("e:\\test\\b1.jpg"));
		}


}
