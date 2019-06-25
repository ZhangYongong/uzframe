package com.ughen.demo;

import com.ughen.util.qiniuyun.QiNiuBucketUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-24
 * Time: 15:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureTest {

    /**
     * 七牛云列举一个账号的所有空间
     */
    @Test
    public void qiniuBucketList() {
        try {
            List<String> list = QiNiuBucketUtil.bucketLsit();
            System.out.println("list = " + list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 阿里云：列举一个账号的所有空间
     */
    @Test
    public void aliBucketList() {
        try {
            List<String> list = QiNiuBucketUtil.bucketLsit();
            System.out.println("list = " + list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
