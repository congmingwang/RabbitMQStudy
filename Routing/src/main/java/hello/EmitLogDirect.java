package hello;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * 
 * @ClassName: EmitLogDirect 
 * @Description: direct exchange
 * @author: wmc
 * @date: 2016年7月21日 下午1:37:30
 */
public class EmitLogDirect {
	
	//exchange名字
	private static final String EXCHANGE_NAME = "direct_logs";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		//创建到RabbitMQ服务器的连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		//创建通道
		Channel channel = connection.createChannel();
		//声明一个通道
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		String severity = getSeverity(args);
		String message = getMessage(args);
		
		//发布消息
		channel.basicPublish(EXCHANGE_NAME, severity, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
		channel.close();
		connection.close();
	}
	
	private static String getMessage(String[] strings){
	    if (strings.length < 1)
	        return "Hello World!";
	    return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
	    int length = strings.length;
	    if (length == 0) return "";
	    StringBuilder words = new StringBuilder(strings[0]);
	    for (int i = 1; i < length; i++) {
	        words.append(delimiter).append(strings[i]);
	    }
	    return words.toString();
	}
	private static String getSeverity(String[] strings){
	    if (strings.length < 1)
	    	    return "info";
	    return strings[0];
	}
}
















