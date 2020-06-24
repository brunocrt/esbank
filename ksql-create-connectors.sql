CREATE SINK CONNECTOR elasticsearch_sink WITH (
      'connector.class' = 'io.confluent.connect.elasticsearch.ElasticsearchSinkConnector',
      'key.ignore'      = 'true',
      'schema.ignore'   = 'true',
      'value.converter' = 'org.apache.kafka.connect.storage.StringConverter',
      'topics'          = 'esbank_transactions',
      'type.name'       = '_doc',
      'drop.invalid.message'= 'true',
      'behavior.on.null.values'= 'ignore',
      'behavior.on.malformed.documents'= 'warn',
      'connection.url'  = 'http://es01:9200',
      'tasks.max'       = '1');

