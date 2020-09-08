    CREATE STREAM income_stream ( 
        user STRING, 
        amount INTEGER, 
        timestamp BIGINT
    ) WITH (KAFKA_TOPIC='deposits', VALUE_FORMAT='DELIMITED', TIMESTAMP='timestamp');

    CREATE STREAM outcome_stream ( 
        user STRING, 
        amount INTEGER, 
        timestamp BIGINT
    ) WITH (KAFKA_TOPIC='withdrawals', VALUE_FORMAT='DELIMITED', TIMESTAMP='timestamp');

    CREATE STREAM transactions_stream ( 
        seq INTEGER, 
        user_from STRING,
        type STRING, 
        user_to STRING,  
        amount INTEGER, 
        timestamp BIGINT
    ) WITH (KAFKA_TOPIC='esbank_transactions', VALUE_FORMAT='DELIMITED', TIMESTAMP='timestamp');
        