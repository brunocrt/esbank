curl http://localhost:9200/_template/transaction_docs -H "Content-Type: application/json" -XPUT --data-ascii "@elastic-index-template-transaction.json"
curl http://localhost:9200/esbank_transactions/_doc/_mapping -H "Content-Type: application/json" -XPUT --data-ascii "@elastic-index-mapping-transaction.json"