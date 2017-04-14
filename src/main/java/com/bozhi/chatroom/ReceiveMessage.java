package com.bozhi.chatroom;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;
public class ReceiveMessage {
	private final static String HOST = "localhost";
	private final static String EXCHANGE_NAME = "fanout";
	private final static String QUEUE = "temp_fanout";
	private final static String ROUTKEY = "mq.fanout";

	public void receive() {
		// 创建连接和频道
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		Consumer consumer = null;
		Channel channel = null;
		try {
			Connection connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			channel.basicQos(1);
			channel.queueBind(QUEUE, EXCHANGE_NAME, ROUTKEY);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					AMQP.BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(message);
			}
		};
		try {
			channel.basicConsume(QUEUE, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
