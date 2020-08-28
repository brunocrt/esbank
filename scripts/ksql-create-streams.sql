    CREATE STREAM transactions_stream ( 
        id INTEGER, 
        user_from STRING, 
        type STRING, 
        user_to STRING,  
        amount INTEGER, 
        timestamp BIGINT
        ) WITH (KAFKA_TOPIC='transactions', VALUE_FORMAT='DELIMITED', TIMESTAMP='timestamp');
        
    CREATE STREAM income_stream ( 
        user STRING, 
        amount INTEGER, 
        timestamp BIGINT
        ) WITH (KAFKA_TOPIC='deposits', PARTITIONS=1, VALUE_FORMAT='DELIMITED', TIMESTAMP='timestamp');

    CREATE STREAM outcome_stream ( 
        user STRING, 
        amount INTEGER, 
        timestamp BIGINT
        ) WITH (KAFKA_TOPIC='withdrawals', PARTITIONS=1, VALUE_FORMAT='DELIMITED', TIMESTAMP='timestamp');
