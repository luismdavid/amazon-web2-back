package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
	
	private static PropertiesReader instance;
	private Properties prop = new Properties();
	
	private PropertiesReader() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("settings.properties")){
			prop.load(input);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getValue(String key) {
		return prop.getProperty(key);
	}
	
	public static PropertiesReader getInstance() {
		if (instance == null) {
			instance = new PropertiesReader();
		}
		return instance;
	}
}
