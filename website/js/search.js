$(document).ready(function() {
	$("#search_button").click(function(){
		var url = "http://localhost:9200/people/_search?q=";
		var query = "";
		var showPerPage = 10;
		var firstResult = 0;
		var size = "&size=" + showPerPage;
		var fromVar = "&from=" + firstResult

		query = $("#search_bar").val();
		window.queryGlobal = query; // TODO:workaround
		url = url + query + size + fromVar;
		console.log(url);

		$('#results').empty(); // clear previous results

		$.getJSON(url, function(result){
			console.log(result);
			var totalHits = result.hits.total;
			var pagesNumber = Math.ceil(totalHits/showPerPage);
			var persons = result.hits.hits;
			// oppure: $.each(result, function(i, field){$("div").append(field + " "); });
			for (var i = 0; i < showPerPage; i++) {
				// TODO: eliminare log in production
				console.log(persons[i]);
				title = persons[i]._source.title;
				url = persons[i]._source.url;
				var regex = /^(https?|ftp):\/\//;
				url = url.replace(regex,'');
				content = persons[i]._source.content;
				indexQuery = content.search(query);
				if (indexQuery!=-1){
					desc1 = content.substring(indexQuery-50, indexQuery);
					descQ = content.substring(indexQuery, indexQuery+query.length);
					descQ = descQ.bold();
					desc2 = content.substring(indexQuery+query.length, indexQuery+query.length+50);
					desc = desc1 + descQ + desc2;
				} else {
					desc = content.substring(0, 100);
				}
				// oppure: $('<a\>', {prop: value}).appendTo('#results');
				var a = '<a class="result-title" href="' + url + '">' + title + '</a>';
				var p = '<p class="result-url">' + url + '</p>';
				var p2 = '<p class="result-desc">' +"..."+ desc + "..." + '</p>';
				$('#results').append('<div class="result">' + a + p + p2 + '</div>');
			}

			var checkBug = 20; //TODO
			for (var i = 0; i < checkBug; i++) {
				var url = "changePage(" + (i+1) + ")";
				var a = '<a class="result-page" href="#" onclick="' + url + '">' + (i+1) + '</a>';
				$('.result-pages').append(a);
				console.log("adad");
			}

		});
	});
});

function changePage(pageNum) {
	//$(".result-page").click(function(){
		var url = "http://localhost:9200/people/_search?q=";
		var query = window.queryGlobal; //TODO:workaround
		var showPerPage = 10;
		var firstResult = (pageNum-1)*showPerPage;
		var size = "&size=" + showPerPage;
		var fromVar = "&from=" + firstResult

		query = $("#search_bar").val();
		url = url + query + size + fromVar;
		console.log(url);

		$('#results').empty(); // clear previous results
		console.log("clearing old results");

		$.getJSON(url, function(result){
			console.log(result);
			var totalHits = result.hits.total;
			var pagesNumber = Math.ceil(totalHits/showPerPage);
			var persons = result.hits.hits;
			// oppure: $.each(result, function(i, field){$("div").append(field + " "); });
			for (var i = 0; i < showPerPage; i++) {
				// TODO: eliminare log in production
				console.log(persons[i]);
				title = persons[i]._source.title;
				url = persons[i]._source.url;
				var regex = /^(https?|ftp):\/\//;
				url = url.replace(regex,'');
				content = persons[i]._source.content;
				indexQuery = content.search(query);
				if (indexQuery!=-1){
					desc1 = content.substring(indexQuery-50, indexQuery);
					descQ = content.substring(indexQuery, indexQuery+query.length);
					descQ = descQ.bold();
					desc2 = content.substring(indexQuery+query.length, indexQuery+query.length+50);
					desc = desc1 + descQ + desc2;
				} else {
					desc = content.substring(0, 100);
				}
				// oppure: $('<a\>', {prop: value}).appendTo('#results');
				var a = '<a class="result-title" href="' + url + '">' + title + '</a>';
				var p = '<p class="result-url">' + url + '</p>';
				var p2 = '<p class="result-desc">' +"..."+ desc + "..." + '</p>';
				$('#results').append('<div class="result">' + a + p + p2 + '</div>');
			}

		});
	//});
}
