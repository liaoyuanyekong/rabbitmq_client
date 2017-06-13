import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by unicom on 2017/6/12.
 */
public class Send {
    private final static  String QUEUE_NAME="hello";

    /**
     * 练习使用java rabbitmq
     */
    public void send() throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection=factory.newConnection();
        //声明管道
        Channel channel=connection.createChannel();
        try{
            /*
             * 消费者和生产者都能使用AMQP的queue.declare来创建队列
             * 声明队列 boolean durable,持久化
             * boolean exclusive, 如果设置成true的话，队列将变成私有的，应用场景：一个队列只有一个消费者
             * boolean autoDelete 如果设置成true的话，当最后一个消费者取消对队列的订阅的时候，队列自动删除
             */
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message="Hello world";
            /*String exchange, String routingKey, BasicProperties props, byte[] body
             *交换器类型：direct
             * exchange 使用一个空白字符串作为默认交换器
             * routingKey 使用队列名称作为路由键 提供规则将队列绑定到对应的exchange
             *
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("[X] send "+message+"");
        }catch (Exception e){

        }finally {
            channel.close();
            connection.close();
        }

    }

    public static void main(String[] args) throws IOException, TimeoutException {
        Send send =new Send();
        send.send();
    }



}
