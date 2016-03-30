package elasticSearch;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import static org.elasticsearch.node.NodeBuilder.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.json.JSONArray;

public class Index {
	public static void main(String[] args) throws IOException {
//		indexGenerator();
//
//	}
//	public static void indexGenerator() throws IOException{
//		Map<String, Object> json = new HashMap<String, Object>();
//		json.put("user","kimchy");
//		json.put("postDate",new Date());
//		json.put("message","trying out Elasticsearch");
//		XContentBuilder builder = jsonBuilder()
//				.startObject()
//				.field("user", "kimchy")
//				.field("postDate", new Date())
//				.field("message", "trying out Elasticsearch")
//				.endObject();
//		// on startup
//		//Node node = nodeBuilder().node();
//	//	Client client = node.client();
////		
////	Node node =NodeBuilder.nodeBuilder().settings(Settings.builder().put("path.home", "/Users/chiara/elasticsearch-2.2.1/data"))
////	    .node();
////		
////	Client client = node.client();
//	
//	// on shutdown
//		//node.close();
//		IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
//				.setSource(jsonBuilder()
//						.startObject()
//						.field("user", "kimchy")
//						.field("postDate", new Date())
//						.field("message", "trying out Elasticsearch")
//						.endObject()
//						)
//						.get();

	}
}
