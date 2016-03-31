var elasticsearch = require('elasticsearch');

var client = new elasticsearch.Client({
  host: 'localhost:9200',
  log: 'trace'
});

client.ping({
  // ping usually has a 3000ms timeout
  requestTimeout: Infinity,

  // undocumented params are appended to the query string
  hello: "elasticsearch!"
}, function (error) {
  if (error) {
    console.trace('elasticsearch cluster is down!');
  } else {
    console.log('All is well');
  }
});

function search() {
	client.search({
	  index: 'people',
	  type: 'person',
	  id: '1'
	}).then(function (resp) {
	    var hits = resp.hits.hits;
	}, function (err) {
	    console.trace(err.message);
	});
}
