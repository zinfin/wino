package sandie.wino;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import sandie.wino.json.JsonWineParser;

public class WineSearchFactory {
	public static  List<sandie.wino.model.List> searchByCategories(HttpClient httpClient, Double [] ids, int resultSize, int offset){
		// Convert double array to concatenate category ids
		String categoryIds = "";
		for (int i=0; i<ids.length; i++){
			categoryIds = categoryIds + ids[i] + "+";
		}
		categoryIds = categoryIds + "490";
		String PARAM = "catalog?filter=categories("+categoryIds+")&offset="+offset+"&size="+resultSize+"&apikey=";
		String searchURL = WineConstants.ENDPOINT+PARAM+WineConstants.API_KEY;
		System.out.println(searchURL);
		return doSearch(httpClient, searchURL,resultSize);
		
	}
	public static  List<sandie.wino.model.List> searchByCategories(HttpClient httpClient, int [] ids, int resultSize, int offset){
		// Convert int array to concatenate category ids
		String categoryIds = "";
		for (int i=0; i<ids.length; i++){
			categoryIds = categoryIds + ids[i] + "+";
		}
		categoryIds = categoryIds + "490";
		String PARAM = "catalog?filter=categories("+categoryIds+")&offset="+offset+"&size="+resultSize+"&apikey=";
		String searchURL = WineConstants.ENDPOINT+PARAM+WineConstants.API_KEY;
		System.out.println(searchURL);
		return doSearch(httpClient, searchURL,resultSize);
		
	}
	public static List<sandie.wino.model.List> searchByCategory(HttpClient httpClient,int id, int resultSize, int offset){
		String PARAM = "catalog?filter=categories(490+"+id+")&offset="+offset+"&size="+resultSize+"&apikey=";
		String searchURL = WineConstants.ENDPOINT+PARAM+WineConstants.API_KEY;
		System.out.println(searchURL);
		return doSearch(httpClient, searchURL,resultSize);
		
	}
	private static List<sandie.wino.model.List> doSearch(HttpClient httpClient, String searchURL, int resultSize){
		HttpGet httpget = new HttpGet(searchURL);
		List<sandie.wino.model.List> productList = new ArrayList<sandie.wino.model.List>();
		try{
			HttpResponse response = httpClient.execute(httpget);
			InputStream data = response.getEntity().getContent();
			JsonWineParser jsonParser = new JsonWineParser();
			jsonParser.setJsonStream(data);
			productList = jsonParser.parseProducts();
		}catch (Exception e){
			e.printStackTrace();
		}
		return productList;
	}
}
