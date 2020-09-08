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

    static String defaultUserFrom = null;
    static int startSequence = 0;

    public static void main(String args[]) {

        logger.info("Starting TxnGenerator...");


        if(args.length > 0) {
            System.out.println("Starting sequence from: "+args[0]);
            startSequence = Integer.parseInt(args[0]);
        }

        if(args.length > 1) {
            System.out.println("Setting default user 'from' to: "+args[1]);
            defaultUserFrom = args[1];
        }

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

            int sequenceNumber = i + startSequence;

            String key = "ID-"+String.valueOf(sequenceNumber);
            String value = buildTransactionData(String.valueOf(sequenceNumber),
                                        getUser(users, true),
                                        getUser(users, false),
                                        getAmount());
            // send transaction
            logger.info("sending message key: {} ", key);
            // ID-1,1,user-1,TRANSFER,user-2,1000,1599589626187
            producer.send(new ProducerRecord<>("esbank_transactions", key, value));


            sleep(1000);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(producer::close));
        
    }

    public static String buildTransactionData(String sequence, String userFrom, String userTo, String amount) {
        return sequence + "," +  // 0, txn sequence
              userFrom + "," +  // 1, txn origin
              "TRANSFER," +     // 2, operation type
              userTo + "," +    // 3, txn destination
              amount + "," +    // 4, operation amount
              new Date().getTime();  // 5, operation timestamp
    }

    public static String getAmount() {
        return String.valueOf(getRandom(MAX_TXN_VALUE));
    }

    public static String getUser(List<String> users, boolean allowDefaultUser) {
        if(allowDefaultUser && defaultUserFrom != null) {
            return defaultUserFrom;
        } else {
            return users.get(getRandom(users.size()));
        }
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