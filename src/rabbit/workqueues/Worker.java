package rabbit.workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by unicom on 2017/6/15.
 */
public class Worker {

    private static final String TASK_QUEUE_NAME="task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("localhost");
        final Connection connection=connectionFactory.newConnection();
        final Channel channel=connection.createChannel();
        channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
        System.out.println(" [*] Waiting for messages ...");
        //消费者发送应答消息，通知RabbitMq没收到应答消息之前最多只能发送一个消息
        channel.basicQos(1);
        final Consumer consumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                String message =new String(body,"UTF-8");
                System.out.println("[x] Received '"+message+" '");
                try{
                    doWork(message);
                }finally {
                    System.out.println(" [X] done");
                    //返回确认状态 如果不返回则RabbitMq一直接受不到ack消息，无法继续为消费者发送新消息。导致队列堆积
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }

            }
        };
        //autoAck 关闭
        channel.basicConsume(TASK_QUEUE_NAME,false,consumer);
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                    System.out.println("sleep 1s ");
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
