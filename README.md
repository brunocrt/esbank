# esbank
Event Streaming Bank

This is an fictional banking system application to demonstrate event streaming architecture using Apache kafka with kafka Streams API.

For more details about Kafka Streams API check the official documentation on [kafka.apache.org!](http://kafka.apache.org).

# Application overview (initial approach [v1])
![ESBank Architecture](esbank-overview.png)
This diagrama presents the initial approach for starting the project with just on client (for generate transaction messages) and two processors (one for filtering using Kafka Streams API and other for creating new views using ksqlDB), additionally I started building a monitor using Kafka Connect embbed into ksqlDB to export data to Elasticsearch. 

The main idea is to starting from this point to reach the state where we can implement all the main components described bellow.

# Main components

**CLIENTS** - Represent the users of the ESBank in order to execute transactions

**CONNECTORS** - They bridge the gaps between clients and the core banking components, they create a secure abstraction to protect and easy integrate all kind of clients.

**PROCESSORS** - They are the core bank components that manage all transactions and actions

**MONITORS** - Monitor any kind of event

[clients] -> [connectors] -> [-- Processors --] <- [connectors] <- [monitors]


## ESBank Clients (Apps)

- ESBank Transaction Generator - Command line client for generate random bank transactions

- ESBank Internet Banking - Web client using React - **future**

## ESBank Connectors (API)

- ESBank Monitor Connector - Elasticsearch Kafka Connect

- ESBank Client Connector - Custom REST API using Java/Quarkus - **future**

## ESBank Processors (Core)

- ESBank Event Store - Apache Kafka Broker components
- ESBank Transaction Manager - Spring Cloud Stream App using Kafka Streams
- ESBank Fraud Detector - KSQLdb Table


## ESBank Monitors (Dashboard)

- ESBank Transaction Dashboard - Elasticsearch + Kibana


## Topics

- transactions
- deposits
- withdrawals
- frauds

### Commands

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

    `list all topics;`
    
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

- KSQL - Query a KSQL Stream

    `SELECT * FROM income_stream EMIT CHANGES;`

- KSQL - Insert into a KSQL Stream

    `INSERT INTO transactions_stream VALUES ('9999',9999,'user-100','TRANSFER','user-200',1000,1591409776299);`

- KSQL - SELECT from KSQL Table

    `SELECT * FROM balance WHERE rowkey='user-1';`
    `SELECT * FROM anomaly EMIT CHANGES;`

- ELASTICSEARCH - Create template
    `curl http://localhost:9200/_template/transaction_docs -H "Content-Type: application/json" -XPUT --data-ascii "@elastic-index-template-transaction.json"`

    `curl http://localhost:9200/esbank_transactions/_doc/_mapping -H "Content-Type: application/json" -XPUT --data-ascii "@elastic-index-mapping-transaction.json"`

- ELASTICSEARCH - View templates
    `curl -XGET http://localhost:9200/_cat/templates?v`