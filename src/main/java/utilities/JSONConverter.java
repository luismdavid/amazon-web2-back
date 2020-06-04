package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONConverter {

	private static String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        Reader inputReader = request.getReader();
	        if (inputReader != null) {
	            bufferedReader = new BufferedReader(inputReader);
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}
	
	public static JSONObject requestParamsToJSON(HttpServletRequest req) {
		JSONParser parser = new JSONParser();
		JSONObject jsonObj;
		try {
			jsonObj = (JSONObject) parser.parse(getBody(req));
			return jsonObj;
		} catch (ParseException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
