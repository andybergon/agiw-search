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
	
	public static String getBingKey(){
		String path = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			path=prop.getProperty("bingKey");
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
