package hello;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
 * @ClassName: ReceiveLogsDirect 
 * @Description: direct exchang接收
 * @author: wmc
 * @date: 2016年7月21日 下午2:06:43
 */
public class ReceiveLogsDirect {
	
	//exchange名字
	private static final String EXCHANGE_NAME = "direct_logs";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		//创建到RabbitMQ服务器的连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		//创建一个通道
		Channel channel = connection.createChannel();
		//声明一个exchange
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		String queueName = channel.queueDeclare().getQueue();
		
		if(args.length < 1){
			 System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
			 System.exit(1);
		}
		
		for(String severity:args){
			channel.queueBind(queueName, EXCHANGE_NAME, severity);
		}
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		//定义消费者
		Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws UnsupportedEncodingException{
				String message = new String(body,"UTF-8");
				System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
	}
}
















