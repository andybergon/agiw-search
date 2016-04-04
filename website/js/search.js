$(document).ready(function() {
  $("#search_button").click(function(){
    var query = "ingegnere";
    var url = "http://localhost:9200/people/_search?q=";
    query = $("#search_bar").val();
    url = url + query;

    $('#results').empty(); // clear previous results

    $.getJSON(url, function(result){
    	var toAppend;
    	console.log(result);
    	var persons = result.hits.hits;
    	// oppure: $.each(result, function(i, field){$("div").append(field + " "); });
    	for (var i = 0, length = persons.length; i < length; i++) {
    		// TODO: eliminare in production
    		console.log(persons[i]);
    		title = persons[i]._source.title;
    		url = persons[i]._source.url;
    		var regex = /^(https?|ftp):\/\//;
    		url = url.replace(regex,'');
				// oppure: $('<a\>', {prop: value}).appendTo('#results');
				var a = '<a class="result-title" href="' + url + '">' + title + '</a>';
				var p = '<p class="result-url">' + url + '</p>';
    		$('#results').append('<div class="result">' + a + p + '</div>');
    	}
    });
	});
});

    // var accessFix = "&callback=?";
    // url = url + accessFix;
    /*
    $.ajax({
      url: url,
      type: "get",
      dataType: 'json',
    })
    .done(function(msg) {
	    if (msg.type === "success") {
	      console.log(msg);
	      console.log("success");
	    } else {
	    	console.log(msg);
	    	console.log("success");
	    }
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
  		console.log("fail");
  		console.log(jqXHR);
  		console.log(textStatus);
  		console.log(errorThrown);
    })
    .always(function(data, textStatus, jqXHR) {
    	console.log("always");
    	console.log(data);
  		console.log(textStatus);
  		console.log(jqXHR);
    });

    // return false;

		$.getJSON(url, function( data ) {
			console.log(data);
		  var items = [];
		  $.each( data, function( key, val ) {
		    items.push( "<li id='" + key + "'>" + val + "</li>" );
		  });

		  $( "<ul/>", {
		    "class": "my-new-list",
		    html: items.join( "" )
		  }).appendTo( "body" );
		});


    var data = {
      size: 5, // get 5 results
      q: 'ingegnere'// query on the title field for 'jones'
    };
    $.ajax({
      url: 'http://localhost:9200/people/_search',
      dataType: 'json',
      success: function(data) {
        alert('Total results found: ' + data.hits.total)
      }
    });
		*/



/*
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
*/
