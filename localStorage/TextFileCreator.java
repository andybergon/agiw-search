package localStorage;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TextFileCreator {

	public static void createFile(String lastname, String name, String url) throws UnsupportedEncodingException{
		Document doc;
		String urlEncoded = URLEncoder.encode(url, "UTF-8");
		String outputFile = PropertiesFile.getStoragePath()+lastname+"_"+name+"_"+urlEncoded+".txt";
		try {
			doc = Jsoup.connect(url).get();
			BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
			htmlWriter.write(doc.toString());
			//Elements newsHeadlines = doc.select("body");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}		
	}
}
