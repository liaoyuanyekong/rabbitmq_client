package rabbit.emitlog;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import rabbit.workqueues.NewTask;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by unicom on 2017/6/15.
 */
public class EmitLogDirect {
    private static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection=connectionFactory.newConnection();
        Channel channel=connection.createChannel();
        //声明交换器和交换模式
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");

        String severity = getSeverity(args);
        String message = getMessage(args);
        //交换器，路由键，设置消息持久化的参数，信息
        channel.basicPublish(EXCHANGE_NAME,severity,null,message.getBytes("UTF-8"));
        System.out.println("Send "+severity+" :"+message +" ");
        channel.close();
        connection.close();

    }


    private static String getSeverity(String[] strings){
        if (strings.length < 1)
            return "info";
        return strings[0];
    }

    private static String getMessage(String[] strings){
        if (strings.length < 2)
            return "Hello World!";
        return joinStrings(strings, " ", 1);
    }

    private static String joinStrings(String[] strings, String delimiter, int startIndex) {
        int length = strings.length;
        if (length == 0 ) return "";
        if (length < startIndex ) return "";
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }


}
