$(document).ready(function() {
	$("#search_button").click(function(){
		var query = "";
		var url = "http://localhost:9200/people/_search?q=";
		query = $("#search_bar").val();
		url = url + query;

		$('#results').empty(); // clear previous results

		$.getJSON(url, function(result){
			var toAppend;
			console.log(result);
			console.log(query);
			var persons = result.hits.hits;
			// oppure: $.each(result, function(i, field){$("div").append(field + " "); });
			for (var i = 0, length = persons.length; i < length; i++) {
				// TODO: eliminare in production
				console.log(persons[i]);
				title = persons[i]._source.title;
				url = persons[i]._source.url;
				content = persons[i]._source.content;
				indexQuery = content.search(query);
				if(indexQuery!=-1){
					desc1 = content.substring(indexQuery-50, indexQuery);
					descQ = content.substring(indexQuery, indexQuery+query.length);
					descQ = descQ.bold();
					desc2 = content.substring(indexQuery+query.length, indexQuery+query.length+50);
					desc = desc1 + descQ + desc2;
				}else{
					desc = content.substring(0, 100);
				}
				var regex = /^(https?|ftp):\/\//;
				url = url.replace(regex,'');
				// oppure: $('<a\>', {prop: value}).appendTo('#results');
				var a = '<a class="result-title" href="' + url + '">' + title + '</a>';
				var p = '<p class="result-url">' + url + '</p>';
				var p2 = '<p class="result-desc">' +"..."+ desc + "..." + '</p>';
				$('#results').append('<div class="result">' + a + p + p2 + '</div>');
			}
		});
	});
});
