package rabbit.emitlog;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by unicom on 2017/6/15.
 */
public class ReceiveLogs {
    private  static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection=connectionFactory.newConnection();
        Channel channel=connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //随机获得一个队列名称
        String queueName=channel.queueDeclare().getQueue();
        //绑定 3个参数分别表示队列名称，交换器名称，路由键名称
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println(" Waiting for message... ");
        Consumer consumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message=new String(body,"UTF-8");
                System.out.println(" Received  '"+message+"'");
            }
        };
        //自动开启应答autoAck=true
        channel.basicConsume(queueName,true,consumer);


    }

}
