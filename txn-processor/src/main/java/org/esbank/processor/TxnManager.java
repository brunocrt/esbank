/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esbank.processor;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class TxnManager {

    static final String SEP = ",";
    static final int SENDER = 1;
    static final int RECEIVER = 3;
    static final int AMOUNT = 4;
    static final int TIMESTAMP = 5;

    public static void main(final String[] args) throws Exception {

        // DEFINING KAFKA BROKER ACCESS CONFIGURATION
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "TxnManager");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // CREATING STREAM PROCESSOR (CONSUMER) BUILDER USING KAFKA STREAMS API
        final StreamsBuilder builder = new StreamsBuilder();

        // setting the topic to be consumed
        final KStream<String, String> transactionsStream =
                builder.<String, String>stream("esbank_transactions");

        // filter events (ignore) where the sender and receiver are the same
        transactionsStream.filter( (key,value) -> !value.split(SEP)[SENDER].equals(value.split(SEP)[RECEIVER]));

        // transform the original transaction data and send it to a new output topic - withdrawals
        transactionsStream.flatMapValues( value -> Arrays.asList(value.split(SEP)[SENDER] + SEP +
                                                                 value.split(SEP)[AMOUNT] + SEP +
                                                                  value.split(SEP)[TIMESTAMP]) )
                .to("withdrawals", Produced.with(Serdes.String(), Serdes.String()));

        // transform the input data and send it to a new output topic - deposits
        transactionsStream
               .flatMapValues( value -> Arrays.asList(value.split(SEP)[RECEIVER] + SEP +
                                                      value.split(SEP)[AMOUNT] + SEP +
                                                      value.split(SEP)[TIMESTAMP]))
               .to("deposits", Produced.with(Serdes.String(), Serdes.String()));

        // create the kafka streams topology which groups all the defined processing logic
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        // start the processing/consuming data streams
        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
