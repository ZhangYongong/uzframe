package com.ughen.model.db;

import java.awt.image.BufferedImage;
import lombok.Data;

/**
 * 验证码实体
 * @author feifei
 */
@Data
public class VerifyCode {

		private String code;
		private BufferedImage bfi;
}
