package hello;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * @ClassName: Send 
 * @Description: RabbitMQ消息发送端,
 * @author: wmc
 * @date: 2016年7月20日 下午12:14:47
 */
public class Send {
	
	private final static String QUEUE_NAME = "hello";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		//创建到RabbitMQ服务器的连接
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		Connection connection = connectionFactory.newConnection();
		//根据连接创建一个通道
		Channel channel = connection.createChannel();
		//声明一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//发布的消息
		String message = "Hello wmc!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '"+message+"'");
		//关闭通道和连接
		channel.close();
		connection.close();
	}
	
}
