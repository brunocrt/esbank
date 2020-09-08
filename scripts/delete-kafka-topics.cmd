docker exec -it broker kafka-topics --bootstrap-server broker:29092 --delete --topic esbank_transactions
docker exec -it broker kafka-topics --bootstrap-server broker:29092 --delete --topic withdrawals
docker exec -it broker kafka-topics --bootstrap-server broker:29092 --delete --topic deposits