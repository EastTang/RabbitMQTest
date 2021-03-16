package com.single2single;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtils {
    private static final String RABBIT_HOST = "localhost";
    private static final String RABBIT_USERNAME = "guest";
    private static final String RABBIT_PASSWORD = "guest";
    private static final ConnectionFactory FACTORY = new ConnectionFactory();
    public static Connection CONNECTION = null;

    public ConnectionUtils() {
        FACTORY.setHost(RABBIT_HOST);
        FACTORY.setUsername(RABBIT_USERNAME);
        FACTORY.setPassword(RABBIT_PASSWORD);
    }

    public static Connection getConnection() {
        if (CONNECTION == null) {
            try {
                CONNECTION = FACTORY.newConnection();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return CONNECTION;
    }
}
