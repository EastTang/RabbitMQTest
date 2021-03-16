package com.single2single;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static final String QUEUE_NAME = "queue001";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection conn = ConnectionUtils.getConnection();
        System.out.println("已获取到的连接：" + conn);

        // 新建通道
        Channel channel = conn.createChannel();

        // 声明队列 （在明确知道本通道有这么一个队列的情况下，可以不声明）
        // （这里可以理解为 消费者可能出现在生产者之前 至少给定一个通道供消费者监听）
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 创建默认的消费者对象DefaultConsumer 并重写其消费方法handleDelivery
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body);
                Node obj = JSON.parseObject(msg, Node.class);
                System.out.printf("消费者%s 接收到数据包：\n\t", consumerTag);
                System.out.println(obj);
            }
        };

        // 监听队列 （队列名，是否自动返回ACK包，消费者对象）
        // 未收到ACK包的消息始终保存于服务端 通过channel.basicAck(deliveryTag, multiple)手动回送ack
        channel.basicConsume(QUEUE_NAME, true, consumer);

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
