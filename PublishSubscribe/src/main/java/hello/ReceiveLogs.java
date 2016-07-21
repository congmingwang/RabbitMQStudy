package hello;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 
 * @ClassName: ReceiveLogs 
 * @Description: 接收日志消息
 * @author: wmc
 * @date: 2016年7月20日 下午11:06:38
 */
public class ReceiveLogs {
	
	//exchange名字
	private static final String EXCHANGE_NAME = "logs";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		//创建一个到RabbitMQ服务器的连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		//创建一个通道
		Channel channel = connection.createChannel();
		//声明exchange
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		//获取到一个临时的队列并将队列名返回
		String queueName = channel.queueDeclare().getQueue();
		//绑定队列到通道
		channel.queueBind(queueName, EXCHANGE_NAME, "");
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		Consumer consumer = new DefaultConsumer(channel){
			 @Override
		      public void handleDelivery(String consumerTag, Envelope envelope,
		                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
		        String message = new String(body, "UTF-8");
		        System.out.println(" [x] Received '" + message + "'");
		      }
		};
		channel.basicConsume(queueName, true,consumer);
	}
}









