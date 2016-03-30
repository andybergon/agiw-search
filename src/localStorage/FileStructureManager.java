package localStorage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;

public class FileStructureManager {
	/**
	 * @param args
	 * @throws IOException 
	 */

	public static void main(String[] args) throws IOException {
		System.out.println("ciao");
		List<Structure> structureList = createStructure("/Users/chiara/Desktop/structure/people.txt");
		createAllFile(structureList);
		serializeStructure(structureList);
		List<Structure> structureList2=deserializeStructure("/Users/chiara/Desktop/structure/structure");
		for(Structure s : structureList2){
			System.out.println(s.getLastname());
			System.out.println(s.getPositionToUrl().keySet());
			}
	}
	public static List<Structure> createStructure(String pathFile) throws IOException{
		List<Structure> structureList = new ArrayList<Structure>();
		List<String> peopleList = PeopleList.peopleList(pathFile);
		AzureSearchWebQuery aq = new AzureSearchWebQuery();
		aq.setAppid("WN2XKNEc30EzahG0zrQwZh20LRK8wGsj1tv/H+xNpUc");
		for (String person : peopleList) {
			String lastname = person.split(" ")[0];
			String name = person.split(" ")[1];
			aq.setQuery(person);
			Structure structure = new Structure();
			structure.setName(name);
			structure.setLastname(lastname);
			Map<Integer, String> positionToUrl = new  HashMap<Integer, String>();
			int cont = 0;
			for (int i=1; i<=1 ; i++) {
				aq.setPage(i);
				aq.doQuery();
				AzureSearchResultSet<AzureSearchWebResult> ars = aq.getQueryResult();
				for (AzureSearchWebResult anr : ars) {
					positionToUrl.put(cont, anr.getUrl());
					cont++;
				}
			}
			structure.setPositionToUrl(positionToUrl);
			structureList.add(structure);
		}
		return structureList;
	}
	
	public static void createAllFile(List<Structure> structureList) throws IOException{
		for (Structure structure : structureList) {
			for (Integer position : structure.getPositionToUrl().keySet()) {
				TextFileCreator.createFile(structure, position);
			}
		}
	}
	
	public static void serializeStructure(List<Structure> structureList){
		try{
	         FileOutputStream fos= new FileOutputStream("/Users/chiara/Desktop/structure/structure");
	         ObjectOutputStream oos= new ObjectOutputStream(fos);
	         oos.writeObject(structureList);
	         oos.close();
	         fos.close();
	       }catch(IOException ioe){
	            ioe.printStackTrace();
	        }
	}
	public static List<Structure> deserializeStructure(String pathFile){
		ArrayList<Structure> structureList= new ArrayList<Structure>();
        try
        {
            FileInputStream fis = new FileInputStream(pathFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            structureList = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
            return structureList;
         }catch(IOException ioe){
             ioe.printStackTrace();
             return null;
          }catch(ClassNotFoundException c){
             System.out.println("Class not found");
             c.printStackTrace();
             return null;
          }
	}
}