package elasticSearch;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
import static org.elasticsearch.node.NodeBuilder.*;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.json.JSONArray;

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

//		GetResponse response = client.prepareGet("customer", "external", "1").get();

//		SearchResponse response = client.prepareSearch("mongoindex")
//			    .setSearchType(SearchType.QUERY_AND_FETCH)
//			    .setQuery(fieldQuery("name", "test name"))
//			    .setFrom(0).setSize(60).setExplain(true)
//			    .execute()
//			    .actionGet();
//		
//			SearchHit[] results = response.getHits().getHits();
//			for (SearchHit hit : results) {
//			  System.out.println(hit.getId());    //prints out the id of the document
//			  Map<String,Object> result = hit.getSource();   //the retrieved document
//			}
			
			
			
//		SearchResponse response = client.prepareSearch("customer")
//		        .setTypes("external")
//		        .setQuery("Jane")
//		        .execute()
//		        .actionGet();
//
//		String printer = response.getHits().toString();
//
//		System.out.println(printer);

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

		// curl 'localhost:9200/people/person/_search/?pretty' - restituisce tutti i nomi del tipo
		
		
		
		
		// on shutdown
		node.close();

	}
	
	public static Map<String, Object> putJsonDocument(String title, String content, URL url) {
		Map<String, Object> jsonDocument = new HashMap<String, Object>();
		jsonDocument.put("title", title);
		jsonDocument.put("content", content);
		jsonDocument.put("url", url);
		return jsonDocument;
	}
	
	
}
