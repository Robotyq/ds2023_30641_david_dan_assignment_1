package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {

    private static final String QUEUE_NAME = "measurementsQueue";
    private static final String RABBITMQ_URL = "amqps://updqfgkc:gEZ_bhBREJUfOMYCQ9RFGCbC5cn_nYJm@goose.rmq2.cloudamqp.com/updqfgkc";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <device_id>");
            System.exit(1);
        }

        String deviceId = args[0];

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(RABBITMQ_URL);

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.queueDeclare(QUEUE_NAME, true, false, false, null);

                try (InputStream inputStream = Main.class.getResourceAsStream("/sensor.csv");
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    long timeMillis = 1000 * 3600 * 2 + System.currentTimeMillis();//- 1000 * 3600 * 48;//acum 48h
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("#")) {
                            break;
                        }
                        float measurementValue = Float.parseFloat(line);

                        ObjectNode jsonNode = objectMapper.createObjectNode();
                        jsonNode.put("timestamp", timeMillis += 1000 * 60 * 5);//+5min
                        jsonNode.put("device_id", deviceId);
                        jsonNode.put("measure", measurementValue);

                        String message = jsonNode.toString();
                        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                        System.out.println(" [x] Sent: " + message);

//                        TimeUnit.SECONDS.sleep(3);
                        TimeUnit.MILLISECONDS.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
