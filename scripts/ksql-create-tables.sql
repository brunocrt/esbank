CREATE TABLE users_income AS SELECT user, count(*) AS txns, sum(amount) AS total FROM income_stream GROUP BY user;

CREATE TABLE users_outcome AS SELECT user, count(*) AS txns, sum(amount) AS total FROM outcome_stream GROUP BY user;

CREATE TABLE balance AS SELECT i.rowkey, sum(i.total)-sum(o.total) AS balance FROM users_income i JOIN users_outcome o ON i.rowkey = o.rowkey GROUP BY i.rowkey;

CREATE TABLE anomalies AS 
        SELECT user_from, COUNT(user_from) as total_txns, 
            sum(amount) as total_amount 
            FROM transactions_stream 
            WINDOW HOPPING (SIZE 10 SECONDS, ADVANCE BY 1 SECONDS) 
            GROUP BY user_from HAVING COUNT(*) > 3;