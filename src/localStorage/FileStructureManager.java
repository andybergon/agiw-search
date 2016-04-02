package localStorage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;

public class FileStructureManager {
	/**
	 * @param args
	 * @throws IOException 
	 */

	public static void main(String[] args) throws IOException {
		System.out.println("Inizio Processamento");
		/*List<Structure> structureList = createStructure(PropertiesFile.getPeoplePath());
		createAllFile(structureList);
		serializeStructure(correctStructure(structureList));
		List<Structure> structureList2=deserializeStructure(PropertiesFile.getStructurePath());
		for(Structure s : structureList2){
			System.out.println(s.getLastname());
			System.out.println(s.getPositionToUrl().keySet());
		}*/
		
		createAllFiles(PropertiesFile.getPeoplePath());
		//deleteEmptyFile();
	}
	public static void deleteEmptyFile() throws UnsupportedEncodingException{
		File dir = new File(PropertiesFile.getStoragePath());
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				// Do something with child
				String fileName = child.getName();
				if(child.length()==0){
					System.out.println("vuoto: "+fileName);
					child.delete();
				}
			}
		} else {
			System.out.println("Storage Path not setted properly, it should be a directory!");
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
		}
	}
	
	public static void createAllFiles(String pathFile) throws IOException{
		List<String> peopleList = PeopleList.peopleList(pathFile);
		AzureSearchWebQuery aq = new AzureSearchWebQuery();
		aq.setAppid(PropertiesFile.getBingKey());
		for (String person : peopleList) {
			String lastname = person.split(" ", 2)[0];
			String name = person.split(" ", 2)[1];
			aq.setMarket("it-IT");
			aq.setQuery(person);

			for (int i=1; i<=8 ; i++) {
				aq.setPage(i);
				aq.doQuery();
				AzureSearchResultSet<AzureSearchWebResult> ars = aq.getQueryResult();
				for (AzureSearchWebResult anr : ars) {
					System.out.println(anr.getUrl());
					TextFileCreator.createFile(lastname, name, anr.getUrl());
				}
			}
		}
	}
	
	public static void directoryIterator() throws UnsupportedEncodingException{
		File dir = new File(PropertiesFile.getStoragePath());
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				// Do something with child
				String fileName = child.getName();
				fileName = fileName.replace(".txt", "");
				String lastname = fileName.split("_")[0];
				String name = fileName.split("_")[1];
				//String url = fileName.split("_")[2];
				int first = fileName.indexOf("_");
				int second = fileName.indexOf("_", first+1);
				String url = fileName.substring(second+1);
				String urlOriginal = URLDecoder.decode(url, "UTF-8");
				System.out.println(lastname+" "+name+" "+urlOriginal);
			}
		} else {
			System.out.println("Storage Path not setted properly, it should be a directory!");
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
		}
	}
}