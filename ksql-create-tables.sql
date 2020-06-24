CREATE TABLE users_income AS SELECT user, count(*) as txns, sum(amount) AS total FROM income_stream GROUP BY user;

CREATE TABLE users_outcome AS SELECT user, count(*) as txns, sum(amount) AS total FROM outcome_stream GROUP BY user;

CREATE TABLE balance AS SELECT i.rowkey, sum(i.total)-sum(o.total) as balance FROM users_income i JOIN users_outcome o ON i.rowkey = o.rowkey GROUP BY i.rowkey;

CREATE TABLE anomaly AS SELECT user_from, sum(amount) as total 
FROM transactions_stream WINDOW TUMBLING (SIZE 10 SECONDS) 
GROUP BY user_from HAVING COUNT(*) > 3;