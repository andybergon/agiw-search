package localStorage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
		PropertiesFile.writePropertiesFile();//andrà rimosso dopo il primo lancio
		List<Structure> structureList = createStructure(PropertiesFile.getPeoplePath());
		createAllFile(structureList);
		serializeStructure(correctStructure(structureList));
		List<Structure> structureList2=deserializeStructure(PropertiesFile.getStructurePath());
		for(Structure s : structureList2){
			System.out.println(s.getLastname());
			System.out.println(s.getPositionToUrl().keySet());
		}
	}
	public static List<Structure> createStructure(String pathFile) throws IOException{
		List<Structure> structureList = new ArrayList<Structure>();
		List<String> peopleList = PeopleList.peopleList(pathFile);
		AzureSearchWebQuery aq = new AzureSearchWebQuery();
		aq.setAppid(PropertiesFile.getBingKey());
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

	public static List<Structure> correctStructure(List<Structure> structureList) throws UnsupportedEncodingException{
		List<Structure> newStructureList = new ArrayList<Structure>() ;
		Structure newStructure = new Structure();
		for (Structure structure : structureList) {
			//newStructure = structure;
			newStructure.setLastname(structure.getLastname());
			newStructure.setName(structure.getName());
			Map<Integer, String> newMap = new HashMap<Integer, String>();
			for (Integer position : structure.getPositionToUrl().keySet()) {
				//se esiste nella cartella il file cognome nome numero (il numero=position)
				//System.out.println(position);
				String urlEncoded = URLEncoder.encode(structure.getPositionToUrl().get(position), "UTF-8");
				File file = new File(PropertiesFile.getStoragePath()+structure.getLastname()+"_"+structure.getName()+"_"+urlEncoded+".txt");
				if(file.exists()){
					newMap.put(position, structure.getPositionToUrl().get(position));
					//					System.out.println(position);
					//					System.out.println(newStructure.getPositionToUrl());
					//					
					//					newStructure.getPositionToUrl().remove(position);
					//					System.out.println(newStructure.getPositionToUrl());
					//					//	newStructureList.get(newStructureList.indexOf(structure)).getPositionToUrl().remove(position);	
					//					System.out.println("non esiste");
					//				}else{

				}
				newStructure.setPositionToUrl(newMap);
				}
			newStructureList.add(newStructure);
		}
		return newStructureList;
	}
	public static void serializeStructure(List<Structure> structureList){
		try{
			FileOutputStream fos= new FileOutputStream(PropertiesFile.getStructurePath());
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