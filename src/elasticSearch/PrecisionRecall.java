/**
 * 
 */
package elasticSearch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLEncoder;

import org.elasticsearch.client.Client;

import localStorage.FileStructureManager;
import localStorage.PropertiesFile;

/**
 * @author Simone
 * Calcola precision e recall
 */
public class PrecisionRecall {

    public static void main(String[] args) throws IOException {
        createDataStructure();
        System.out.println(calculatePrecision("Giovanna Contini"));
        System.out.println(calculateRecall("Giovanna Contini"));

    }

    /* crea la struttura dati che sarà un file txt contenente coppie chiave valore Url1 - persona1, Url2 - persona2, ...
     * (persona1 sarà cognome1_nome1) */
    public static void createDataStructure() throws IOException{
        File directory = new File(PropertiesFile.getStoragePath()); //seleziono la directory "storageProva"
        File outputFile = new File(PropertiesFile.getStructurePath()+"PrecisionRecallDataStructure.txt");
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        /*apro il FileWriter */
        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        /* itero sui file dentro la directory */
        for(File f : directory.listFiles()){
            if (!f.getName().equals(".DS_Store")){ //scarto .DS_Store che è un file nascosto che mi crea nella directory
                String path = f.getName(); //prendo il nome del file che sarà "cognome_nome_url.txt"
                String lastname = path.split("_", 3)[0];
                String name = path.split("_", 3)[1];
                String url = path.split("_",3)[2];
                url = url.replace(".txt", ""); //tolgo il ".txt" finale 
                bw.write(url+" - "+lastname+"_"+name+"\n"); 
            }
        }
        bw.close();  
    }


    /* creo una struttura dati tipo indice inverso ovvero una mappa Persona (Nome Cognome), lista di url
     * per farlo sfrutto la struttura creata nel metodo precedente */
    public static Map<String, List<String>> createMapPersonUrlList() throws IOException{
        Map<String,List<String>> map = new HashMap<String, List<String>>();
        FileReader fileReader = new FileReader(PropertiesFile.getStructurePath()+"PrecisionRecallDataStructure.txt");
        BufferedReader bufferReader = new BufferedReader(fileReader);
        String thisLine;
        while ((thisLine = bufferReader.readLine()) != null) {
            String url = thisLine.split(" -",2)[0];
            String lastname_name = thisLine.split(" -",2)[1];
            String lastname= lastname_name.split("_",2)[0];
            String name= lastname_name.split("_",2)[1];
            String key = name+lastname;
            List<String> newUrlList = map.get(key);
            if(newUrlList==null){ 
                newUrlList = new ArrayList<String>();
            }
            newUrlList.add(url);
            map.put(key, newUrlList);
        }  
        bufferReader.close();
        return map;
    }


    /* calcolo la precision (quanti rilevanti tra i recuperati/quanti recuperati) */  
    public static double calculatePrecision(String query) throws IOException{
        /* faccio la query */
        Client client = TClient.getClient();
        List<String> urlListQuery = TClient.searchDocument(client, "people", "person", query);
        List<String> urlList = createMapPersonUrlList().get(query);
        int contRelevant = 0;
        /* itero sugli url ritornati dalla query e verifico quanti di essi si trovano nella lista 
         * di url derivata dalla struttura Url1 - persona1 per quella persona, ecc..  ovvero quanti sono RILEVANTI*/
        for(String url : urlListQuery){
            String urlEncoded = URLEncoder.encode(url, "UTF-8"); //le url nella urlListQuery non sono encodate, le encodo e verifico l'uguaglianza
            if(urlList.contains(urlEncoded)){
                contRelevant = contRelevant+1;
            }
        }        
        System.out.println(contRelevant);
        System.out.println(urlListQuery.size());
        // calcolo la precision (quanti rilevanti tra i recuperati/quanti recuperati)
        double relevant = (double)(contRelevant);
        return  relevant / (urlListQuery.size()); 
    }



    /* calcolo la recall (quanti rilevanti tra i recuperati/quanti rilevanti)  
     * il numeratore è lo stesso della 
     * pressuppongo che la query sia "nome cognome"*/
    public static double calculateRecall(String query) throws IOException{
        /* faccio la query */
        Client client = TClient.getClient();
        List<String> urlListQuery = TClient.searchDocument(client, "people", "person", query);
        List<String> urlList = createMapPersonUrlList().get(query);
        int contRelevant = 0;
        /* itero sugli url ritornati dalla query e verifico quanti di essi si trovano nella lista 
         * di url derivata dalla struttura Url1 - persona1, ecc..   ovvero quanti sono RILEVANTI*/
        for(String url : urlListQuery){
            String urlEncoded = URLEncoder.encode(url, "UTF-8"); //le url nella urlListQuery non sono encodate, le encodo e verifico l'uguaglianza
            if(urlList.contains(urlEncoded)){
                contRelevant++;
            }
        }        
        System.out.println(contRelevant);
        
        // per trovare i rilevanti in generale sfrutto la struttura dati per la recall
        List<String> listRelevant = createMapPersonUrlList().get(query);
        System.out.println(listRelevant.size());
        double relevant = (double)(contRelevant);
        return  relevant / (listRelevant.size()); 
    }
}

