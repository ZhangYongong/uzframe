package com.ughen.controller;

import java.util.Random;
import org.junit.Test;

public class VerifyCodeControllerTest {

		public static final char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
				'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
				'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		@Test
		public void getStrVerifyCode() {
				String str = "";
				Random random = new Random();
				for (int i = 0; i < 6; i++) {
						char num = ch[random.nextInt(ch.length)];
						str += num;
				}
				System.out.println(str);
		}

		@Test
		public void getStreamCode() {
		}

		@Test
		public void checkVerifyCode() {
		}
}