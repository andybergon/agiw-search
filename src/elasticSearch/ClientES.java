package elasticSearch;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
import static org.elasticsearch.node.NodeBuilder.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
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

		GetResponse response = client.prepareGet("customer", "external", "1").get();

		String printer = response.getSourceAsString();

		System.out.println(printer);

		// on shutdown
		node.close();

	}
}
