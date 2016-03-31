ElasticSearchClient = require('elasticsearchclient');

var serverOptions = {
    host: 'localhost',
    port: 9200,
    pathPrefix:'optional pathPrefix',
    secure: true||false,
    //Optional basic HTTP Auth
    auth: {
    username: process.env.ES_USERNAME,
        password: process.env.ES_PASSWORD
  }
};
