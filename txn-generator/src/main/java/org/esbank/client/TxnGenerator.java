package org.esbank.client;

import org.apache.kafka.clients.producer.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxnGenerator {

    static final Logger logger = LoggerFactory.getLogger(TxnGenerator.class);

    static final int TOTAL_USERS = 10;
    static final int MAX_TXN_VALUE = 1000;
    static final int TOTAL_TXNS = 1000;

    public static void main(String args[]) {

        logger.info("Starting TxnGenerator...");

        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "TxnGenerator");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "20000");
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "500");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        logger.info("Gernerating users data...");
        List<String> users = generateUsers(TOTAL_USERS);

        logger.info("Gernerating transactions data...");
        for(int i=0; i < TOTAL_TXNS; i++) {

            String key = "ID-"+String.valueOf(i);
            String value = buildTransactionData(key,
                                        users.get(getRandom(users.size())),
                                        users.get(getRandom(users.size())),
                                        String.valueOf(getRandom(MAX_TXN_VALUE)));
            // send transaction
            logger.info("sending message key: {} ", key);
            producer.send(new ProducerRecord<>("esbank_transactions", key, value));


            sleep(1000);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(producer::close));
        
    }


    public static String buildTransactionData(String id, String userFrom, String userTo, String amount) {
        return id + "," +       // 0, txn id
              userFrom + "," +  // 1, txn origin
              "TRANSFER," +     // 2, operation type
              userTo + "," +    // 3, txn destination
              amount + "," +    // 4, operation amount
              new Date().getTime();  // 5, operation timestamp
    }
    
    public static List<String> generateUsers(int totalUsers) {
        List<String> users = new ArrayList<String>();
        for(int i=0; i<totalUsers; i++) {
            users.add("user-"+i);
        }
        return users;
    }

    public static int getRandom(int maxValue) {
        sleep(100);
        return ((new Random()).nextInt(maxValue));
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}