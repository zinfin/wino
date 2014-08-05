package sandie.wino.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import sandie.wino.WineConstants;
import sandie.wino.model.Category;
import sandie.wino.model.Products;
import sandie.wino.model.WineApiJsonParser;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonWineParser implements WineApiJsonParser {

	private InputStream jsonStream;
	public JsonWineParser(){
		
	}

	public InputStream getJsonStream() {
		return jsonStream;
	}

	public void setJsonStream(InputStream jsonStream) {
		this.jsonStream = jsonStream;
	}

	public static List<Category> parseCategories(InputStream json) throws Exception{
		List<Category> categories = new ArrayList<Category>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(json));
	      StringBuilder sb = new StringBuilder();

	      try {
	         String line = reader.readLine();
	         while (line != null) {
	            sb.append(line);
	            line = reader.readLine();
	         }
	      } catch (IOException e) {
	         throw e;
	      } finally {
	         reader.close();
	      }
		return categories;
	}
	private String getJSONString(InputStream jsonStream){
		BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
		StringBuilder sb = new StringBuilder();
		
		try {
			String line = reader.readLine();
			while (line != null) {
				sb.append(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			try {
				reader.close();
			} catch (IOException e1) {
				Log.d(WineConstants.LOG_TAG, e1.getLocalizedMessage());
				throw new RuntimeException(e);
			}
			Log.d(WineConstants.LOG_TAG, e.getLocalizedMessage());
			throw new RuntimeException(e);
		}
        return sb.toString();
	}
	@Override
	public ArrayList<Category> parseCategories() {
		List<Category> categories = new ArrayList<Category>();
		String line = getJSONString(jsonStream);
        JSONObject jo;
		try {
			jo = new JSONObject(line);
			Object p = jo.get("Categories");
			ObjectMapper mapper = new ObjectMapper();
			categories = mapper.readValue(p.toString(),new TypeReference<ArrayList<Category>>() { });
			
		} catch (JSONException e) {
			Log.d(WineConstants.LOG_TAG, e.getMessage());
		} catch (JsonMappingException jme){
			Log.d(WineConstants.LOG_TAG, jme.getMessage());
		} catch (JsonParseException pe){
			Log.d(WineConstants.LOG_TAG, pe.getMessage());
		} catch (IOException io){
			Log.d(WineConstants.LOG_TAG, io.getMessage());
		}   
        
		return (ArrayList<Category>) categories;
	}

	@Override
	public ArrayList<sandie.wino.model.List> parseProducts() {
		Products product = new Products();
		String line = getJSONString(jsonStream);
        JSONObject jo;
		try {
			jo = new JSONObject(line);
			Object p = jo.get("Products");
			ObjectMapper mapper = new ObjectMapper();
			product = mapper.readValue(p.toString(),Products.class);
			
		} catch (JSONException e) {
			Log.d(WineConstants.LOG_TAG, e.getMessage());
		} catch (JsonMappingException jme){
			Log.d(WineConstants.LOG_TAG, jme.getMessage());
		} catch (JsonParseException pe){
			Log.d(WineConstants.LOG_TAG, pe.getMessage());
		} catch (IOException io){
			Log.d(WineConstants.LOG_TAG, io.getMessage());
		} 
		return (ArrayList<sandie.wino.model.List>)product.getList();
	}
}
