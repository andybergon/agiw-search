package localStorage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;


public class Structure implements Serializable{
	private String name;
	private String lastname;
	private Map<Integer, String> positionToUrl;
	
	
	public Structure(String name, String lastname,
			Map<Integer, String> positionToUrl) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.positionToUrl = positionToUrl;
	}

	public Structure(){}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public Map<Integer, String> getPositionToUrl() {
		return positionToUrl;
	}

	public void setPositionToUrl(Map<Integer, String> positionToUrl) {
		this.positionToUrl = positionToUrl;
	}

	//da aggiustare
	public void serialize() throws IOException{
        FileOutputStream fileOut = new FileOutputStream("/Users/chiara/Desktop/structure/structures.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
        fileOut.close();
    }

}