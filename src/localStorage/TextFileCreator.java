package localStorage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.xml.ws.http.HTTPException;

/**
 * @author chiara
 *
 */
public class TextFileCreator {


	/**Il metodo prende in input un URL, apre la pagina e restituisce l'intero corpo della pagina HTML
	 * @param URL
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String getHTML(String URL) throws MalformedURLException{

		SSLTool.disableCertificateValidation();
		URLConnection connection;
		try {
			connection = new URL(URL).openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();
			BufferedReader in  = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
			String inputLine;
			String page = "";

			while ((inputLine = in.readLine()) != null){
				page+=inputLine+"\n";
			}
			in.close();
			return page;

		} catch (HTTPException e) {
			System.out.println("http ex");
			return null;

		} catch (FileNotFoundException e) {
			System.out.println("file not found ex");
			return null;

		} catch (IOException e ) {
			System.out.println("io ex");
			e.printStackTrace();
			return null;
		}

	}

	public static void createFile(Structure structure, int position) throws IOException{
		String urlFile = structure.getPositionToUrl().get(position);
		String name = structure.getName();
		String lastname = structure.getLastname();
		
		String html = getHTML(urlFile);

		if(html==null || html.isEmpty()){
			return;
		}
		//<html lang="it"
		String language = html;
		language = language.substring(language.indexOf("lang=")+6);
		language = language.substring(0, 2);
		System.out.println(language);
		if(!language.equals("it"))
			return;
		
		//		String html = title;
		//		title = title.substring(title.indexOf("<title>") + 7);
		//      title = title.substring(0, title.indexOf("</title>"));
		File file = new File("/Users/chiara/Desktop/storage/"+lastname+"_"+name+"_"+position+".txt");
		if(!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(html);
		bw.close();		
	}
}
