package rabbit.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by unicom on 2017/6/15.
 */
public class NewTask {
    private static final String TASK_QUEUE_NAME="task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection=factory.newConnection();
        //创建信道
        Channel channel=connection.createChannel();
        //声明队列
        channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
        String message=getMessage(args);
        //当同时打开2个消费者的时候可以发现基本上平分消息的
        for(int i=0;i<10;i++){
            //发布持久化消息
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("[x] send '"+message+"'");
        }
        channel.close();
        connection.close();
    }

   public static String getMessage(String[] strings) {
       // if (strings.length < 1)
            return "Hello World!";
      //  return joinStrings(strings, " ");
    }
    public static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }



}
