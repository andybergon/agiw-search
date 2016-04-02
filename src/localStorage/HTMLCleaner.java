package localStorage;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 
 * Classe che pulisce dall HTML una pagina passandogli URL 
 * (ho messo che restituisce il bodi ma si può impostare tutto usando jsoup 
 * aggiungete il jar)
 *
 */
public class HTMLCleaner {

	public static void fileCleaner(String url, String outputFile){
		Document doc;
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
