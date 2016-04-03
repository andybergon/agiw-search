package localStorage;

import java.io.BufferedWriter;
import java.io.File;
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

	public static String getBody(String s) throws IOException{
		/*Document doc;
			doc = Jsoup.connect(url).get();
			Elements body = doc.select("body");*/
		File input = new File(s);
		Document doc = Jsoup.parse(input, "UTF-8", "");

		//System.out.println("body= "+doc.select("body").text());
		return doc.select("body").text();
	}

	public static String getTitle(String url) throws IOException{
		Elements body=Jsoup.parse(url).getElementsByAttribute("title");
		System.out.println("body= "+body.toString());
		return body.toString();		
	}
}
