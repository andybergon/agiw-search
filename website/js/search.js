var baseUrl = "http://localhost:9200/people/_search?q=";
var query, firstResult, showPerPage, maxPages, url, size, fromVar, currentPage;
var totalHits, pagesNumber, persons, pagesToDisplay;
var maxTitleLength, maxUrlLength, maxDescriptionLength;
var regexUrl;

showPerPage = 10;
maxPages = 20;
size = "&size=" + showPerPage;

maxTitleLength = 60;
maxDescriptionLength = 200;
maxUrlLength = 60;

regexUrl = new RegExp("^(https?|ftp)://"); // /^(https?|ftp):\/\//;

$(document).ready(function() {
	$('#search-button').click(function(){
		query = $('#search-bar').val().toLowerCase(); // check if result changes with lowercase
		currentPage = 1;
		getES();
	});
	$('#search-bar').keypress(function(e){
		if(e.which == 13){ // enter button
			$('#search-button').click();
		}
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

			// TITOLO
			title = persons[i]._source.title;
			if (title.length > maxTitleLength) {
				var start = title.substring(0, maxTitleLength);
				var lastSpaceToVisIndex = title.substring(maxTitleLength).indexOf(" ");
				var end = title.substring(maxTitleLength, maxTitleLength + lastSpaceToVisIndex + 1);
				title = start + end + " ...";
			}

			// URL
			url = persons[i]._source.url;
			var urlClean = url.replace(regexUrl,'');
			if (urlClean.length > maxUrlLength) {
				urlClean = urlClean.substring(0, maxUrlLength);
				urlClean += "...";
			}

			// DESCRIPTION
			var content = persons[i]._source.content;
			formatDescription(content);

			// oppure: $('<a\>', {prop: value}).appendTo('#results');
			var a = '<a class="result-title" href="' + url + '">' + title + '</a>';
			var p = '<p class="result-url">' + urlClean + '</p>';
			var p2 = '<p class="result-desc">' + desc + '</p>';
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

function formatDescription(content) {
	var queryWords = query.split(" ");
	var indexQuery = content.search(new RegExp(queryWords.join('|'),'ig'));
	var regexDescription = new RegExp('(\\b)(' + queryWords.join('|') + ')(\\b)','ig');

	if (indexQuery == -1) {

		if (content.length > maxDescriptionLength) {
			desc = content.substring(0, maxDescriptionLength);
			desc = desc + "...";
		}

	} else if (indexQuery == 0) {

		if (content.length > maxDescriptionLength) {
			desc = content.substring(0, maxDescriptionLength);
			desc = desc + "...";
		}
		desc = desc.replace(regexDescription, '$1<b>$2</b>$3');

	} else {

		var mdl2 = maxDescriptionLength/2;
		if(indexQuery >= mdl2) {
			var descCore = content.substring(indexQuery - mdl2, indexQuery + mdl2);
			descCore = descCore.replace(regexDescription, '$1<b>$2</b>$3');
			desc = "..." + descCore;
			if (content.length - indexQuery > maxDescriptionLength) {
				desc = desc + "...";
			}
		} else {
			if (content.length > maxDescriptionLength) {
				desc = content.substring(0, maxDescriptionLength);
			}
			desc = desc.replace(regexDescription, '$1<b>$2</b>$3');
		}

	}
}
