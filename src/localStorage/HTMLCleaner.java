package localStorage;

import java.io.IOException;

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

	public static String fileCleaner(String url){
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			Elements newsHeadlines = doc.select("body");
			return newsHeadlines.text();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
