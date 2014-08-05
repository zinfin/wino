package sandie.wino.test.JSON;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import sandie.wino.WineConstants;
import sandie.wino.json.JsonWineParser;
import sandie.wino.model.Category;
import sandie.wino.model.Ratings;
import sandie.wino.model.Refinement;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParsingTest {
	private static HttpClient httpClient;
	
	@BeforeClass
	public static void method() {
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		HttpParams connManagerParams = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(connManagerParams, 5);
		ConnManagerParams.setMaxConnectionsPerRoute(connManagerParams,
				new ConnPerRouteBean(5));
		ConnManagerParams.setTimeout(connManagerParams, 15 * 1000);

		ThreadSafeClientConnManager cm =
				new ThreadSafeClientConnManager(connManagerParams,
						schemeRegistry);

		HttpParams clientParams = new BasicHttpParams();
		HttpProtocolParams.setUserAgent(clientParams, "WinoApp/1.0");
		HttpConnectionParams.setConnectionTimeout(clientParams, 15 * 1000);
		HttpConnectionParams.setSoTimeout(clientParams, 15 * 1000);
		httpClient = new DefaultHttpClient(cm, clientParams);
	}
	@Test
	final public void testParse(){
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(new File("/home/sandie/workspace/android_samples/Wino/src-test/sandie/wino/test/JSON/categories.txt")));
			final StringBuilder sb = new StringBuilder();

			try {
				String line = reader.readLine();
				while (line != null) {
					sb.append(line);
					line = reader.readLine();
				}
				reader.close();
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
			final JSONObject jo = new JSONObject(sb.toString());
			final Object p = jo.get("Categories");
			final List<Category> categories = mapper.readValue(p.toString(),new TypeReference<ArrayList<Category>>() { });
			if (categories!=null){
				for (Category category : categories){
					System.out.println(category.getName() + ", " + category.getDescription()+" , "+ category.getId());
					List<Refinement> refs = category.getRefinements();
					for (Refinement ref : refs){
						System.out.println("-----"+ref.getId()+ " " + ref.getName());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	@Test
	/**
	 * The following query returns all red wines from the wine finder, 
	 * starting at record #16, with a list size of 100 product
	 */
	final public void testSearchRedWineProduct(){	
		final List<sandie.wino.model.List> productList = searchByCategory(124,100,15);
		final sandie.wino.model.List item = productList.get(0);
		assertTrue(validateNameAndRegion(item));
		assertTrue(item.getVarietal().getWineType().getName().equalsIgnoreCase("Red Wines"));
		printProductList(productList);
	}
	/**
	 * Find wines from the year 2009 (374)
	 */
	@Test
	public void testSearchForVintage(){
		final List<sandie.wino.model.List> productList = searchByCategory(374,50,6);
		final sandie.wino.model.List item = productList.get(0);
		assertTrue(validateNameAndRegion(item));
		// Search for 2009 in name
		assertTrue(item.getName().matches(".*2009$"));
		printProductList(productList);
	}
	/**
	 * Find some Cabernet varietals
	 * 139 is the id for Cabernet Sauvignon
	 */
	@Test
	final public void testSearchForVarietal(){
		final List<sandie.wino.model.List> productList = searchByCategory(139,10,5);
		final sandie.wino.model.List item = productList.get(0);
		final String varietal = item.getVarietal().getName();
		assertTrue(varietal.equalsIgnoreCase("Cabernet Sauvignon"));
		assertTrue(validateNameAndRegion(item));
		printProductList(productList);
	}
	/**
	 * Find Oregon wines
	 * Willamette Valley (2474)
	 */
	@Test
	final public void testSearchByRegion(){
		final List<sandie.wino.model.List> productList = searchByCategory(2474,13,6);
		final sandie.wino.model.List item = productList.get(0);
		assertTrue(validateNameAndRegion(item));
		assertTrue(item.getAppellation().getRegion().getName().equalsIgnoreCase("Oregon"));
		printProductList(productList);
	}
	/**
	 * Find wines that go with Cheese (3009)
	 */
	@Test
	final public void testSearchByFood(){
		final List<sandie.wino.model.List> productList = searchByCategory(3009,10,2);
		final sandie.wino.model.List item = productList.get(0);
		assertTrue(validateNameAndRegion(item));;
		printProductList(productList);
	}
	/**
	 * Wines from Walla Walla Valley (1956)
	 */
	@Test
	final public void testSearchByAppellation(){
		final List<sandie.wino.model.List> productList = searchByCategory(1956,3,2);
		final sandie.wino.model.List item = productList.get(0);
		assertTrue(validateNameAndRegion(item));
		assertTrue(item.getAppellation().getRegion().getName().equalsIgnoreCase("Washington"));
		assertTrue(item.getAppellation().getName().equalsIgnoreCase("Walla Walla Valley"));
		printProductList(productList);
	}
	/**
	 * Test with a combination of two parameters, a specific appellation and vintage
	 */
	@Test
	final public void testByAppellationAndVintage(){
		final int[] ids = new int[2];
		ids[0]= 1956;
		ids[1] = 374;
		final List<sandie.wino.model.List> productList = searchByCategories(ids, 3, 2);
		printProductList(productList);
	}
	/**
	 * Test for a certain style of white wine that is rich and creamy
	 * 616.0 White - Rich & Creamy
	 */
	@Test
	final public void testByWineStyle(){
		final List<sandie.wino.model.List> productList = searchByCategory(616,3,2);
		final sandie.wino.model.List item = productList.get(0);
		assertTrue(validateNameAndRegion(item));
		printProductList(productList);
	}
	@Test
	final public void testForZinRavenswood(){
		final int[] ids = new int[3];
		ids[0]= 2371;
		ids[1] = 141;
		ids[2] = 377;
		final List<sandie.wino.model.List> productList = searchByCategories(ids, 50, 1);
		printProductList(productList);

	}
	/**
	 * Perform a category search with given list category ids and return a result of given size offset by desired value.
	 * @param ids - an array of int representing <sandie.wino.model.list> ids
	 * @param resultSize - an int representing the desired size of result set
	 * @param offset - an int representing the desired offset of the results
	 * @return a list of <sandie.wino.model.List> items
	 */
	final private List<sandie.wino.model.List> searchByCategories(int [] ids, int resultSize, int offset){
		// Convert int array to concatenate category ids
		String categoryIds = "";
		for (int i=0; i<ids.length; i++){
			categoryIds = categoryIds + ids[i] + "+";
		}
		categoryIds = categoryIds + "490";
		final String PARAM = "catalog?filter=categories("+categoryIds+")&offset="+offset+"&size="+resultSize+"&apikey=";
		final String searchURL = WineConstants.ENDPOINT+PARAM+WineConstants.API_KEY;
		System.out.println(searchURL);
		return doSearch(searchURL);	
	}

	/**
	 * Perform a category search with a category id and return a result of given size offset by desired value.
	 * @param id - an int representing <sandie.wino.model.list> id
	 * @param resultSize - an int representing the desired size of result set
	 * @param offset - an int representing the desired offset of the results
	 * @return a list of <sandie.wino.model.List> items
	 * @param id
	 * @param resultSize
	 * @param offset
	 * @return
	 */
	final private List<sandie.wino.model.List> searchByCategory(int id, int resultSize, int offset){
		final String PARAM = "catalog?filter=categories(490+"+id+")&offset="+offset+"&size="+resultSize+"&apikey=";
		final String searchURL = WineConstants.ENDPOINT+PARAM+WineConstants.API_KEY;
		System.out.println(searchURL);
		return doSearch(searchURL);		
	}
	/**
	 * Execute the search against the given URL
	 * @param searchURL
	 * @return list of <sandie.winod.model.List> items
	 */
	final private List<sandie.wino.model.List> doSearch(String searchURL){
		final HttpGet httpget = new HttpGet(searchURL);
		List<sandie.wino.model.List> productList = new ArrayList<sandie.wino.model.List>();
		try{
			final HttpResponse response = httpClient.execute(httpget);
			final InputStream data = response.getEntity().getContent();
			final JsonWineParser jsonParser = new JsonWineParser();
			jsonParser.setJsonStream(data);
			productList = jsonParser.parseProducts();
		}catch (Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
		return productList;
	}
	final private boolean validateNameAndRegion(sandie.wino.model.List item ){
		return (item.getName()!=null && item.getAppellation().getRegion()!=null);
	}
	/**
	 * Print product names
	 * @param list
	 */
	final private void printProductList(List<sandie.wino.model.List> list){
		String varietal, region, type, name;
		Ratings ratings;
		double score =0.0;
		for (sandie.wino.model.List item: list){
			name = item.getName();
			varietal = item.getVarietal().getName();
			region = item.getAppellation().getRegion().getName();
			type = item.getVarietal().getWineType().getName();
			ratings = item.getRatings();
			
			if (ratings!=null){
				score = ratings.getHighestScore();
			}
			System.out.println ("N: "+name + " VA: " + varietal + " R:" + region+ " T:" + type + " SC:"+score);
			
		}
	}
}
