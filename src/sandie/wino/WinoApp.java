package sandie.wino;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sandie.wino.json.JsonWineParser;
import sandie.wino.model.Category;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

public class WinoApp extends Application {
	//Include data members shared by application
	private ConnectivityManager cMgr;
	private JsonWineParser jsonParser;
	private List<Category> categories = new ArrayList<Category>();
	private List<sandie.wino.model.List> results = new ArrayList<sandie.wino.model.List>();
	private Map<String,Integer> selectedItems;
	private int resultSize = 10;
	private int offset = 5;
	private Map<Double, Bitmap> imageCache;
	
	public Map<Double, Bitmap> getImageCache() {
		return imageCache;
	}

	// getters/setters
	public JsonWineParser getJsonParser() {
		return jsonParser;
	}

	public List<Category> getSearchCategories(){
		return this.categories;
	}
	public void setSearchCategories(List<Category> categories){
		Log.d(WineConstants.LOG_TAG,"Search categories set");
		this.categories = categories;
	}
	@Override
	public void onCreate(){
		super.onCreate();
		this.cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		this.jsonParser = new JsonWineParser();
	    this.imageCache = new HashMap<Double, Bitmap>();
	}
	@Override
	public void onTerminate() {
		// not guaranteed to be called
		super.onTerminate();
	}
	public boolean connectionPresent() {
		NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
		if ((netInfo != null) && (netInfo.getState() != null)) {
			return netInfo.getState().equals(State.CONNECTED);
		} 
		return false;
	}

	public List<sandie.wino.model.List> getResults() {
		return results;
	}

	public void setResults(List<sandie.wino.model.List> results) {
		this.results = results;
	}

	public Map<String,Integer> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(Map<String,Integer> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	public Bitmap retrieveBitmap(String urlString) {
		Log.d(WineConstants.LOG_TAG, "making HTTP trip for image:" + urlString);
		Bitmap bitmap = null;
		try {
			URL url = new URL(urlString);
			// NOTE, be careful about just doing "url.openStream()"  
			// it's a shortcut for openConnection().getInputStream() and doesn't set timeouts
			// (the defaults are "infinite" so it will wait forever if endpoint server is down)
			// do it properly with a few more lines of code . . .         
			URLConnection conn = url.openConnection();     
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(5000);         
			bitmap = BitmapFactory.decodeStream(conn.getInputStream());
		} catch (MalformedURLException e) {
			Log.e(WineConstants.LOG_TAG, "Exception loading image, malformed URL", e);
		} catch (IOException e) {
			Log.e(WineConstants.LOG_TAG, "Exception loading image, IO error", e);
		} 
		return bitmap;
	}

}
