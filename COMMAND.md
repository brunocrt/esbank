# Commands

- Create a kafka topic:

    `docker exec -it broker kafka-topics --bootstrap-server broker:29092 --create --partitions 1 --replication-factor 1 --topic esbank_transactions`

- Consume a kafka topic:

    `docker exec -it broker kafka-console-consumer --bootstrap-server broker:29092 --topic esbank_transactions`

- Delete a kafka topic:

    `docker exec -it broker kafka-topics --bootstrap-server broker:29092 --delete --topic esbank_transactions`

- List all kafka topics:

    `docker exec -it broker kafka-topics --bootstrap-server broker:29092 --list`

- Copy Elasticsearch sink connectors to
    `docker cp confluentinc-kafka-connect-elasticsearch-5.5.0 ksqldb-server:/usr/share/kafka/plugins/`

- Run KSQL cli

    `docker exec -it ksqldb-cli ksql http://ksqldb-server:8088`
    
- KSQL List all topics

    `SHOW TOPICS;`

- Show the contents of a Kafka topic
    `PRINT 'topic_name' FROM BEGINNING;`
    
- KSQL - create KSQL streams from file

    First you do need to copy the file to the container using
    `docker cp ksql-create-streams.sql ksqldb-server:/tmp` and then run it inside the KSQL cli:
    
    `RUN SCRIPT '/tmp/ksql-create-streams.sql';`

- KSQL -  create KSQL tables from file
    First you do need to copy the file to the container using
    `docker cp ksql-create-tables.sql ksqldb-server:/tmp` and then run it inside the KSQL cli:

   `RUN SCRIPT '/tmp/ksql-create-tables.sql';`

- KSQL List all streams and tables;

    `SHOW STREAMS;`
    `SHOW TABLES;`

- KSQL - Query a KSQL Stream

    `SELECT * FROM income_stream EMIT CHANGES;`

- KSQL - Insert into a KSQL Stream

    `INSERT INTO transactions_stream VALUES ('9999',9999,'user-100','TRANSFER','user-200',1000,1591409776299);`

- KSQL - Drop a KSQL Stream

    You do need to terminate all read/write queries attached to the stream first before drop the stream as the following example:
    `TERMINATE ctas_anomalies_11;`
    `DROP stream transactions_stream;`

- KSQL - SELECT from KSQL Table

    `SELECT * FROM balance WHERE rowkey='user-1';`
    `SELECT * FROM anomalies EMIT CHANGES;`

- ELASTICSEARCH - Create template
    `curl http://localhost:9200/_template/transaction_docs -H "Content-Type: application/json" -XPUT --data-ascii "@elastic-index-template-transaction.json"`

    `curl http://localhost:9200/esbank_transactions/_doc/_mapping -H "Content-Type: application/json" -XPUT --data-ascii "@elastic-index-mapping-transaction.json"`

- ELASTICSEARCH - View templates
    `curl -XGET http://localhost:9200/_cat/templates?v`


## Resources

- KSQL SQL queries  
    More information about KSQL queries can be found at https://cnfl.io/queries