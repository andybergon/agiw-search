package elasticSearch;

import static org.elasticsearch.node.NodeBuilder.*;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;

import localStorage.HTMLCleaner;
import localStorage.PeopleList;
import localStorage.PropertiesFile;
import localStorage.TextFileCreator;
import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;

public class ClientES {
	public static void main(String[] args) throws IOException {
		clientGetter();
	}

	public static void clientGetter() throws IOException {
/*
		Settings settings = Settings.settingsBuilder().loadFromSource(jsonBuilder()
                .startObject()
                .startObject("analysis")
                    .startObject("filter")
                        .startObject("test_filter_stopwords_it")
                            .field("type", "stop")
                            .field("stopwords_path", "stopwords/stop_it")
                        .endObject()
                        .startObject("test_filter_snowball_it")
                            .field("type", "snowball")
                            .field("language", "Italian")
                        .endObject()
                        .startObject("test_filter_worddelimiter_it")
                            .field("type", "word_delimiter")
                            .field("protected_words_path", "worddelimiters/protectedwords_it")
                            .field("type_table_path", "typetable")
                        .endObject()
                        .startObject("test_filter_synonyms_it")
                            .field("type", "synonym")
                            .field("synonyms_path", "synonyms/synonyms_it")
                            .field("ignore_case", true)
                            .field("expand", true)
                        .endObject()
                        .startObject("test_filter_ngram")
                            .field("type", "edgeNGram")
                            .field("min_gram", 2)
                            .field("max_gram", 30)
                        .endObject()
                   .endObject()
                   .startObject("analyzer")
                        .startObject("test_analyzer")
                            .field("type", "custom")
                            .field("tokenizer", "whitespace")
                            .field("filter", new String[]{"lowercase",
                                                                             "test_filter_worddelimiter_it",
                                                                             "test_filter_stopwords_it",
                                                                             "test_filter_synonyms_it",
                                                                             "test_filter_snowball_it"})
                            .field("char_filter", "html_strip")
                        .endObject()
                   .endObject()
                .endObject()
            .endObject().string()).build();
*/
		// on startup
		Node node = nodeBuilder().clusterName("elasticsearch").settings(Settings.settingsBuilder()
				.put("http.enabled", false).put("path.home", "/home/andybergon/elasticsearch-2.2.1/data")).client(true)
				.node();

		Client client = node.client();
		/*CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate("people");
		createIndexRequestBuilder.setSettings(settings);*/
		directoryIterator(client);
		/*
		client.prepareIndex("people", "person", "1")
				.setSource(putJsonDocument("andrea rossi",
						"andrea rossi imprenditore milanese lavora campo big data ingegnere filosofo",
						new URL("https://www.andrearossi.it")))
				.execute().actionGet().isCreated();

		client.prepareIndex("people", "person", "2")
				.setSource(putJsonDocument("andrea bergonzo",
						"andrea bergonzo ingegnere romano lavora campo machine learning ingegnere",
						new URL("https://www.andrearossi.it")))
				.execute().actionGet();

		String idSearched = "1";
		GetResponse getResponse = client.prepareGet("people", "person", idSearched).execute().actionGet();
		printPerson(getResponse);
		 */

		//searchDocument(client, "people", "person", "ingegnere");
		//searchDocumentForField(client, "people", "person", "content", "ingegnere");
		
		// on shutdown
		node.close();

	}
	
	public static void directoryIterator(Client client) throws IOException, UnsupportedEncodingException{
		File dir = new File(PropertiesFile.getStoragePath());
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				// Do something with child
				String fileName = child.getName();
				fileName = fileName.replace(".txt", "");
				String lastname = fileName.split("_")[0];
				String name = fileName.split("_")[1];
				//String url = fileName.split("_")[2];
				int first = fileName.indexOf("_");
				int second = fileName.indexOf("_", first+1);
				String url = fileName.substring(second+1);
				String urlOriginal = URLDecoder.decode(url, "UTF-8");
				System.out.println(lastname+" "+name+" "+urlOriginal);
				
				/*String json = "{" +"\"name\":\""+name+"\"," +
				        "\"lastname\":\""+lastname+"\"," +
				        "\"body\":\""+HTMLCleaner.getBody(child.getAbsolutePath())+"\"" +
				        "\"url\":\""+urlOriginal+"\"," +
				    "}";

				client.prepareIndex("people", "person")
				        .setSource(json)
				        .execute().actionGet().isCreated();*/
				client.prepareIndex("people", "person")
				.setSource(putJsonDocument(name+" "+lastname, HTMLCleaner.getBody(child.getAbsolutePath()),	
						new URL(urlOriginal)))
				.execute().actionGet().isCreated();
			}
		} else {
			System.out.println("Storage Path not setted properly, it should be a directory!");
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
		}
	}
	
	public static void searchDocument(Client client, String index, String type, String value) {
		
		SearchResponse response = client.prepareSearch(index)
				.setTypes(type)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setQuery(QueryBuilders.queryStringQuery("ingegnere"))
				.setFrom(0)
				.setSize(60)
				.setExplain(true)
				.execute()
				.actionGet();
		
		SearchHit[] results = response.getHits().getHits();
		
		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			System.out.println(hit.getScore() + result.toString());
		}
	}

	public static void searchDocumentForField(Client client, String index, String type, String field, String value) {

		SearchResponse response = client.prepareSearch(index)
				.setTypes(type)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setQuery(QueryBuilders.queryStringQuery("ingegnere"))
				.setFrom(0)
				.setSize(60)
				.setExplain(true)
				.execute()
				.actionGet();

		SearchHit[] results = response.getHits().getHits();
		
		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			System.out.println(result);
		}
	}
	

	public static Map<String, Object> putJsonDocument(String title, String content, URL url) {
		Map<String, Object> jsonDocument = new HashMap<String, Object>();
		jsonDocument.put("title", title);
		jsonDocument.put("content", content);
		jsonDocument.put("url", url);
		return jsonDocument;
	}

	public static void printPerson(GetResponse getResponse) {

		Map<String, Object> source = getResponse.getSource();

		System.out.println("------------------------------");
		System.out.println("Index: " + getResponse.getIndex());
		System.out.println("Type: " + getResponse.getType());
		System.out.println("Id: " + getResponse.getId());
		System.out.println("Version: " + getResponse.getVersion());
		System.out.println(source);
		System.out.println("------------------------------");
	}

}
