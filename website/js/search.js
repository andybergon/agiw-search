var baseUrl = "http://localhost:9200/people/_search?q=";
var query, firstResult, showPerPage, maxPages, url, size, fromVar, currentPage;
var totalHits, pagesNumber, persons, pagesToDisplay, maxTitleLength;

showPerPage = 10;
maxPages = 20;
size = "&size=" + showPerPage;

maxTitleLength = 60;

$(document).ready(function() {
	$("#search_button").click(function(){
		query = $("#search_bar").val();
		currentPage = 1;
		getES();
	});
});

function getES() {
	firstResult = (currentPage-1) * showPerPage;
	fromVar = "&from=" + firstResult;
	url = baseUrl + query + size + fromVar;
	console.log(url);
	$.getJSON(url, function(result){
		$('#results').empty(); // clear previous results
		$('.result-pages').empty();
		console.log(result);
		// con db statico le prossime 2 potrei non ricalcolarle
		totalHits = result.hits.total;
		pagesNumber = Math.ceil(totalHits/showPerPage);
		persons = result.hits.hits;
		// oppure: $.each(result, function(i, field){$("div").append(field + " "); });
		for (var i = 0; i < persons.length; i++) {
			console.log(persons[i]);
			title = persons[i]._source.title;
			// cut too long titles
			if (title.length > maxTitleLength) {
				var start = title.substring(0, maxTitleLength);
				var lastSpaceToVisIndex = title.substring(maxTitleLength).indexOf(" ");
				var end = title.substring(maxTitleLength, maxTitleLength + lastSpaceToVisIndex + 1);
				title = start + end + " ...";
			}
			url = persons[i]._source.url;
			var regex = /^(https?|ftp):\/\//;
			var urlClean = url.replace(regex,'');
			content = persons[i]._source.content;
			// var queryString = new RegExp("^" + query + " | " + query + " ");
			queryString = " " + query + " "; // TODO: se è all'inizio del testo non funziona
			indexQuery = content.search(queryString);
			if (indexQuery!=-1){
				desc1 = content.substring(indexQuery-50, indexQuery);
				descQ = content.substring(indexQuery, indexQuery+query.length+2);
				descQ = descQ.bold();
				desc2 = content.substring(indexQuery+query.length, indexQuery+query.length+50);
				desc = desc1 + descQ + desc2;
			} else {
				desc = content.substring(0, 100);
			}
			// oppure: $('<a\>', {prop: value}).appendTo('#results');
			var a = '<a class="result-title" href="' + url + '">' + title + '</a>';
			var p = '<p class="result-url">' + urlClean + '</p>';
			var p2 = '<p class="result-desc">' +"..."+ desc + "..." + '</p>';
			$('#results').append('<div class="result">' + a + p + p2 + '</div>');
		}

		// can be done simply modifing the css instead of recalculating it every time
		pagesToDisplay = pagesNumber < maxPages ? pagesNumber : maxPages;
		for (var i = 1; i <= pagesToDisplay; i++) {
			var url, a, inactive;
			url = "changePage(" + i + ")";
			inactive = (i == currentPage) ? " inactive-link" : "";
			a = '<a class="result-page' + inactive + '" href="#" onclick="' + url + '">' + i + '</a>';
			$('.result-pages').append(a);
		}
		console.log("pages appended");
	});
}

function changePage(pageNum) {
		currentPage = pageNum;
		getES();
}
