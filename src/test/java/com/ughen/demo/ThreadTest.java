package com.ughen.demo;

import com.ughen.util.executor.ThreadUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @Description: 测试类
 * @Author: Yonghong Zhang
 * @Date: 2019-02-13
 * @Time: 15:24
 */
public class ThreadTest {
    @Test
    public void threadTest() throws Exception{
        long start = System.currentTimeMillis();
        List<Future> futureList = new ArrayList();
        // 发送10次消息
        for (int i = 0; i < 10; i++) {
            try {
                String msg = String.format("这是第{%s}条消息", i);
                Future<String> messageFuture = ThreadUtil.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        System.out.println(String.format("打印消息%s", msg));
                        return System.currentTimeMillis()+"";
                    }
                });
                futureList.add(messageFuture);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Future<String> message : futureList) {
            String messageData = message.get();
            System.out.println("messageData = " + messageData);
        }
        System.out.println(String.format("共计耗时{%s}毫秒", System.currentTimeMillis() - start));
    }
}
