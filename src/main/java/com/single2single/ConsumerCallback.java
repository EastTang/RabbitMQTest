package com.single2single;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerCallback {
    public static final String QUEUE_NAME = "queue001";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection conn = ConnectionUtils.getConnection();
        System.out.println("已获取到的连接：" + conn);

        // 新建通道
        Channel channel = conn.createChannel();

        // 声明队列 （在明确知道本通道有这么一个队列的情况下，可以不声明）
        // （这里可以理解为至少给定一个通道供消费者监听）
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 创建该通道的消费者回调 根据官方文档使用deliverCallback
        DeliverCallback deliverCallback = (consumeTag, delivery) -> {
            String msg = new String(delivery.getBody());
            Node obj = JSON.parseObject(msg, Node.class);
            System.out.printf("消费者%s 接收到数据包：\n\t", consumeTag);
            System.out.println(obj);
        };

        // 监听队列 （队列名，是否自动返回ACK包，消费回调，取消回调）
        // 未收到ACK包的消息始终保存于服务端 通过channel.basicAck(deliveryTag, multiple)手动回送ack
        String s = channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            System.out.println("Cannel···");
        });

        // 释放资源 消费者停止监听 消费者回调不在本线程
        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channel.close();
        conn.close();*/
    }
}
