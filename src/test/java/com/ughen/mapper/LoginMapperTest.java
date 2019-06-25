package com.ughen.mapper;

import com.ughen.model.db.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginMapperTest {

		@Autowired
		private LoginMapper mapper;

		@Test
		public void insert() {
				System.out.println("测试新增");
				User u = new User();
				u.setMobile("15190411498");
				u.setPassword("123456");
				u.setPlatform("uzTest");
//				u.setDevice("PC");
//				mapper.insert(u);
				Assert.assertEquals(1, mapper.insert(u));
		}

		@Test
		public void select() {
		}

		@Test
		public void update() {
		}

		@Test
		public void delete() {
//				Assert.assertEquals(1, mapper.update(1););
		}
}