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
			// TODO: handle exception
			return null;
		
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			return null;
		
		} catch (IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	public static void createFile(String urlFile, String name, String lastname, int position) throws IOException{
		String html = getHTML(urlFile);
		if(html==null)
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
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		//createFile("https://it.wikipedia.org/wiki/Ciao");
		List<String> peopleListLista = PeopleList.peopleList("/Users/chiara/Desktop/structure/people.txt");
		System.out.println(peopleListLista.toString());
	}
}
