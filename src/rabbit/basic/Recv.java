package rabbit.basic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by unicom on 2017/6/13.
 */
public class Recv {
    public final static String QUEUE_NAME="no_mirror_durable";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        //factory.setHost("localhost");
        factory.setUsername("wzh");//指定用户
        factory.setPassword("wzh");//用户密码
        factory.setVirtualHost("wzh"); //指定vhost
        /*factory.setHost("192.168.7.222");//指定rabbitmq服务器的主机
        Connection connection=factory.newConnection();*/

        Address[] addresses=new Address[]{new Address("192.168.7.222"),new Address("192.168.7.223"),new Address("192.168.7.224")};
        Connection connection=factory.newConnection(addresses);

        //声明信道
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        System.out.println(" [*] waiting for messages to exit press CTRL+C");
        channel.basicQos(1);
        Consumer consumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                try{
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [X] done");
                    //返回确认状态 如果不返回则RabbitMq一直接受不到ack消息，无法继续为消费者发送新消息。导致队列堆积
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME,false,consumer);


    }
}
