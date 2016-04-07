/**
 * 
 */
package elasticSearch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import localStorage.FileStructureManager;
import localStorage.PropertiesFile;

/**
 * @author Simone
 * Calcola precision e recall
 */
public class PrecisionRecall {

    public static void main(String[] args) {
        try {
            createDataStructure();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
    
    /*public static double calculatePrecision(String query){
        double precision = 0;
        
        
        return precision;
    }*/
}

