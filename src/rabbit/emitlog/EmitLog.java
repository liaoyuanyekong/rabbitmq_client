package rabbit.emitlog;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import rabbit.workqueues.NewTask;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by unicom on 2017/6/15.
 */
public class EmitLog {
    private static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection=connectionFactory.newConnection();
        Channel channnel=connection.createChannel();
        //声明交换器,并声明fanout类型，广播类型所有队列都含有发送的消息，所以不需要指定队列名称
        channnel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //获取要发送消息
        String messsage=NewTask.getMessage(args);
        //发布消息
        channnel.basicPublish(EXCHANGE_NAME,"",null,messsage.getBytes("UTF-8"));
        System.out.println("send  '"+messsage+"'");
        channnel.close();
        connection.close();
    }
}
