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
import java.util.List;
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

        System.out.println(calculatePrecision("giovanna contini"));
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

        /* itero sulle directory (storageChiara, storageLuca) dentro la directory "storageProva" */
        for(File dir : directory.listFiles()){      
            if (!dir.getName().equals(".DS_Store")){ //scarto .DS_Store che è un file nascosto che mi crea nella directory
                /* itero sui file dentro la directory */
                for(File f : dir.listFiles()){
                    if (!f.getName().equals(".DS_Store")){ //scarto .DS_Store che è un file nascosto che mi crea nella directory
                        String path = f.getName(); //prendo il nome del file che sarà "cognome_nome_url.txt"
                        String lastname = path.split("_", 3)[0];
                        String name = path.split("_", 3)[1];
                        String url = path.split("_",3)[2];
                        url = url.replace(".txt", ""); //tolgo il ".txt" finale 
                        bw.write(url+" - "+lastname+"_"+name+"\n"); 
                    }
                }
            }
        }
        bw.close();  
    }


    /* Seleziono tutti gli url nel file PrecisionRecallDataStructure e li inserisco in una lista */
    public static List<String> getURLfromDataStructure() throws IOException{
        List<String> urlList = new ArrayList<String>();
        FileReader fileReader = new FileReader(PropertiesFile.getStructurePath()+"PrecisionRecallDataStructure.txt");
        BufferedReader bufferReader = new BufferedReader(fileReader);
        while (bufferReader.readLine() != null) {
            String line = bufferReader.readLine();
            String url = line.split(" -",2)[0];
            urlList.add(url);
        }  
        bufferReader.close();
        return urlList;
    }



    public static double calculatePrecision(String query) throws IOException{
        /* faccio la query */
        Client client = TClient.getClient();
        List<String> urlListQuery = TClient.searchDocument(client, "people", "person", query);
        List<String> urlList = getURLfromDataStructure();
        int contRelevant = 0;
        /* itero sugli url ritornati dalla query e verifico quanti di essi si trovano nella lista 
         * di url derivata dalla struttura Url1 - persona1, ecc..   ovvero quanti sono RILEVANTI*/
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
}

