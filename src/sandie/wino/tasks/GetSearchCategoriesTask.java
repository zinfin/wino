package sandie.wino.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import sandie.wino.WineConstants;
import sandie.wino.WinoApp;
import sandie.wino.activities.ShowSearchOptionsActivity;
import sandie.wino.interfaces.OnTaskCompleted;
import sandie.wino.json.JsonWineParser;
import sandie.wino.model.Category;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

public class GetSearchCategoriesTask extends AsyncTask<String, Void, List<Category>> {

	private static String PARAM = "categorymap?filter=categories(490)&apikey=";
	private WinoApp _app;
	private OnTaskCompleted _taskCompleted;
	
	public GetSearchCategoriesTask(Application application, OnTaskCompleted activity){
		_app = (WinoApp) application;
		_taskCompleted = activity;

	}
	public void connectContext(OnTaskCompleted context){
		this._taskCompleted = context;
	}
	public void disconnectContext(){
		this._taskCompleted = null;
	}
	@Override
	protected List<Category> doInBackground(String... params) {
		List<Category> categories = new ArrayList<Category>();
		HttpClient httpClient = ShowSearchOptionsActivity.getHttpClient();
		String categoryURL = WineConstants.ENDPOINT+PARAM+WineConstants.API_KEY;
		Log.d(WineConstants.LOG_TAG, categoryURL);
		HttpGet httpget = new HttpGet(categoryURL);
		try {
			HttpResponse response = httpClient.execute(httpget);
			InputStream data = response.getEntity().getContent();
			JsonWineParser jsonParser = _app.getJsonParser();
			jsonParser.setJsonStream(data);
			categories = jsonParser.parseCategories();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return categories;
	}
		
	@Override
	protected void onPostExecute(List<Category> categories){
		_app.setSearchCategories(categories);
		Log.d(WineConstants.LOG_TAG, "Doing async post execute");
		_taskCompleted.callback();		
	}

}
