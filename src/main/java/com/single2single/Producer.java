package com.single2single;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static final String QUEUE_NAME = "queue001";

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // 获取连接
        Connection conn = ConnectionUtils.getConnection();
        System.out.println("已获取到的连接：" + conn);

        // 新建通道  异常IOException直接抛出
        Channel channel = conn.createChannel();

        // 声明队列 （队列名称，持久化/重启不删除，单个连接独占，无用时删除，其它参数）
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 传递数据
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int total = 100;
        for (int i = 1; i <= total; i++) {
            String message = String.format("{\"title\":\"%s\", \"text\":\"%s\", \"id\":%d, \"time\":\"%s\"}", "title" + i, "Hello World!" + i, i, df.format(new Date()));
            // 发送数据 （交换机名，队列名，消息头，消息体byte[]）
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("--> 已发送数据包 id=" + i);
            // 休眠 异常InterruptedException抛出
            Thread.sleep(1000);
        }

        // 传送结束
        System.out.println("生产者结束数据发送");
        // 资源释放  异常TimeoutException抛出
        channel.close();
        conn.close();
    }
}
