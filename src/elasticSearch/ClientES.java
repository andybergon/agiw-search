package elasticSearch;

import static org.elasticsearch.node.NodeBuilder.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;

import localStorage.HTMLCleaner;
import localStorage.PropertiesFile;


public class ClientES {
	public static void main(String[] args) throws IOException {
		clientGetter();
	}

	public static void clientGetter() throws IOException {

		// on startup
		Node node = nodeBuilder()
				.clusterName("elasticsearch")
				.settings(Settings.settingsBuilder()
				.put("http.enabled", false)
				.put("path.home", "/home/andybergon/elasticsearch-2.2.1/data"))
				.client(true)
				.node();

		Client client = node.client();
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
			for (File file : directoryListing) {
				// Do something with child
				String fileName = file.getName();
				fileName = fileName.replace(".txt", "");
				String[] fileInfo = fileName.split("_",3);
				String url = fileInfo[2];
				String urlDecoded = URLDecoder.decode(url, "UTF-8");
				String filePath = file.getAbsolutePath();
				String[] data = HTMLCleaner.getTitleAndBody(filePath);
				String title = data[0];
				String body = data[1];
				client.prepareIndex("people", "person")
						.setSource(putJsonDocument(title, body,	new URL(urlDecoded)))
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
