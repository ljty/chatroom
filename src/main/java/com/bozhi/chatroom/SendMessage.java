package com.bozhi.chatroom;


import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;


public class SendMessage {
	private final static String HOST = "localhost";
	private final static String EXCHANGE_NAME = "fanout";
	private final static String QUEUE = "temp_fanout";
	private final static String ROUTKEY = "mq.fanout";

	public void send() {
		// 创建连接和频道
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);

		Connection connection = null;
		Channel channel = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			// 声明转发器和类型
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			channel.basicQos(1);
			channel.queueBind(QUEUE, EXCHANGE_NAME, ROUTKEY);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		System.out.println("welcome to rabbitmq chatroom ^-^");

		System.out.println("type q to quit....");

		System.out.println("please  input your name:");

	
		Scanner scanner = new Scanner(System.in);
		String  name = scanner.next();
		System.out.println("what you want to say:");
		String message = null;

		while (true) {
		
			
			String text = scanner.next();
			if ("q".equals(text)) {
				System.exit(0);
			} else {
				message = name + " said: " + text;
			}
			// 往转发器上发送消息
			try {
				channel.basicPublish(EXCHANGE_NAME, "",
						MessageProperties.PERSISTENT_TEXT_PLAIN,
						message.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	
}
