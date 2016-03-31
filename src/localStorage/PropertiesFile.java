package localStorage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
/**
 * 
 * @author Luca Massuda
 * Serve per leggere e scrivere un file di configurazione 
 * al fine di caricare il percorso dei file csv.
 */
public class PropertiesFile {

	public static void writePropertiesFile(){
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			output = new FileOutputStream("config.properties");
			// set the properties value
			prop.setProperty("peoplePath", "//Users//chiara//Desktop//structure//people.txt");
			prop.setProperty("structurePath", "//Users//chiara//Desktop//structure//structure");
			prop.setProperty("storagePath", "//Users//chiara//Desktop//storage//");
			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void readPropertiesFile(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			System.out.println(prop.getProperty("database"));
			System.out.println(prop.getProperty("dbuser"));
			System.out.println(prop.getProperty("dbpassword"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getPeoplePath(){
		String path = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			path=prop.getProperty("peoplePath");
			return path;
		} catch (IOException ex) {
			ex.printStackTrace();
			return path;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getStructurePath(){
		String path = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			path=prop.getProperty("structurePath");
			return path;
		} catch (IOException ex) {
			ex.printStackTrace();
			return path;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static String getStoragePath(){
		String path = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			path=prop.getProperty("storagePath");
			return path;
		} catch (IOException ex) {
			ex.printStackTrace();
			return path;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
