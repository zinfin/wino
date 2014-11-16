package sandie.wino.activities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import sandie.wino.R;
import sandie.wino.WinoApp;
import sandie.wino.fragment.GetSearchCriteriaFragment;
import sandie.wino.model.Category;
import sandie.wino.model.Refinement;
import sandie.wino.view.NoDefaultSpinner;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class ShowSearchOptionsActivity extends Activity implements GetSearchCriteriaFragment.TaskCallbacks{
//public class ShowSearchOptionsActivity extends Activity implements OnTaskCompleted{
	private static final AbstractHttpClient httpClient;
	private static final String WINE_TYPE = "Wine Type";
	private static final String VARIETAL = "Varietal";
	private static final String WINE_STYLE= "Wine Style";
	private static final String REGION = "REGION";
	private static final String VINTAGE = "Vintage";
	private static final String FOOD_TYPE = "Food Type";
	private static final String APPELLATION = "Appellation";
	private GetSearchCriteriaFragment mCriteriaFragment;
	private static final boolean DEBUG = true; 
	private static final String TAG_TASK_FRAGMENT = GetSearchCriteriaFragment.class.getName() ;
	
	private NoDefaultSpinner wineTypeSpinner, varietalSpinner, wineStyleSpinner,
	regionSpinner,vintageSpinner,foodSpinner, appellationSpinner;

	private Button searchBtn, resetBtn;
	
	private ProgressDialog progressDialog;
	private WinoApp app;
	private Map<String,Integer> selectedItems;
	
	private static final HttpRequestRetryHandler retryHandler;

	static {
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

		retryHandler = new DefaultHttpRequestRetryHandler(5, false) {

			public boolean retryRequest(IOException exception, int executionCount,
					HttpContext context) {
				if (!super.retryRequest(exception, executionCount, context)) {
					Log.d("HTTP retry-handler", "Won't retry");
					return false;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				Log.d("HTTP retry-handler", "Retrying request...");
				return true;
			}
		};

		httpClient.setHttpRequestRetryHandler(retryHandler);
	}

	public static HttpClient getHttpClient() {
		return httpClient;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		if (DEBUG) Log.i(TAG_TASK_FRAGMENT, "onCreate()");
		// Start by showing the search options
		setContentView(R.layout.activity_show_search);
		
		// Show a message that data is loading while the search filters are loading
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getString(R.string.show_search_activity_retrieving_data));
		progressDialog.show();
		
		// Use Application object for app wide state
		app = (WinoApp) getApplication();
		
		FragmentManager fm = getFragmentManager();
	    mCriteriaFragment = (GetSearchCriteriaFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
	    // If the Fragment is non-null, then it is currently being
	    // retained across a configuration change.
	    if (mCriteriaFragment == null) {
	      mCriteriaFragment = new GetSearchCriteriaFragment();
	      fm.beginTransaction().add(mCriteriaFragment, TAG_TASK_FRAGMENT).commit();
	    }
	
		// Get search category list (parsing if needed)
		if (app.getSearchCategories().isEmpty()){			
			if (app.connectionPresent()){
				if (mCriteriaFragment.isRunning()) {
			        mCriteriaFragment.cancel();
			    } else {
			    	mCriteriaFragment.start(app);
			    }
			}else{
				Toast.makeText(this, getString(R.string.show_search_activity_network_unavailable), Toast.LENGTH_LONG).show();
			}
		}else{
			setUpDropdowns();
			if (progressDialog.isShowing()){
				progressDialog.cancel();
			}
		}
		// Add click listener to search button
		searchBtn = (Button) findViewById(R.id.searchWineBtn);
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if (selectedItems !=null){		
					// Save the items to the app
					app.setSelectedItems(selectedItems);
									
					Intent searchResults = new Intent(ShowSearchOptionsActivity.this, ShowSearchResultsActivity.class);
					startActivity(searchResults);
				}				
			}
		} );
		// Add click listener for the reset button
		resetBtn = (Button) findViewById(R.id.resetBtn);
		resetBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchBtn.setEnabled(false);
				setUpDropdowns();
				selectedItems = null;
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	/**
	 * Set up drop downs for view
	 */
	protected void setUpDropdowns(){
		
		if (app.getSearchCategories()!=null & !app.getSearchCategories().isEmpty()){
			
			// Extract specific categories and populate appropriate spinners with refinements
			for (Category category : app.getSearchCategories()){
				List<Refinement> items = category.getRefinements();
//				List<String> items = new ArrayList<String>();
//				for (Refinement r: refinements){
//					items.add(r.getName());
//				}
				// Sort
				//Collections.sort(items);
				if (category.getName().equalsIgnoreCase(WINE_TYPE)){
					wineTypeSpinner=(NoDefaultSpinner) findViewById(R.id.wine_type_spinner);
					assignSpinner(wineTypeSpinner, items);
				}
				if (category.getName().equalsIgnoreCase(VARIETAL)){
					varietalSpinner=(NoDefaultSpinner) findViewById(R.id.varietal_spinner);
					assignSpinner(varietalSpinner, items);
				}
				if (category.getName().equalsIgnoreCase(WINE_STYLE)){
					wineStyleSpinner=(NoDefaultSpinner) findViewById(R.id.wine_style_spinner);
					assignSpinner(wineStyleSpinner, items);
				}	
				if (category.getName().equalsIgnoreCase(REGION)){
					regionSpinner=(NoDefaultSpinner) findViewById(R.id.region_spinner);
					assignSpinner(regionSpinner, items);
				}
				if (category.getName().equalsIgnoreCase(VINTAGE)){
					vintageSpinner=(NoDefaultSpinner) findViewById(R.id.vintage_spinner);
					assignSpinner(vintageSpinner, items);
				}
				if (category.getName().equalsIgnoreCase(FOOD_TYPE)){
					foodSpinner=(NoDefaultSpinner) findViewById(R.id.food_type_spinner);
					assignSpinner(foodSpinner, items);
				}
				if (category.getName().equalsIgnoreCase(APPELLATION)){
					appellationSpinner=(NoDefaultSpinner) findViewById(R.id.apellation_spinner);
					assignSpinner(appellationSpinner, items);
				}
			}
		}
	}
	protected void assignSpinner(final NoDefaultSpinner spinner, List<Refinement> list){
		ArrayAdapter<Refinement>  spinnerAdapter =
				new ArrayAdapter<Refinement>(this, android.R.layout.simple_spinner_item, list);

		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	         @Override
	         public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	        	 // Store a hash of spinner name to selected index
	        	 if (selectedItems==null){
	        		 selectedItems = new HashMap<String, Integer>();
	        		 
	        	 }
	        	 searchBtn.setEnabled(true);
	        	 String key = null;
	        	 int spinnerId = parentView.getId();
	        	 if (spinnerId ==wineStyleSpinner.getId()){
	        		 key = WINE_STYLE;
	        		 
	        	 }if (spinnerId == wineTypeSpinner.getId()) {
	        		 key = WINE_TYPE;
	        	 }else if(spinnerId == regionSpinner.getId()){
	        		 key = REGION;
	        	 }else if (spinnerId == vintageSpinner.getId()){
	        		 key = VINTAGE;
	        	 }else if (spinnerId == foodSpinner.getId()){
	        		 key = FOOD_TYPE;
	        	 }else if (spinnerId == appellationSpinner.getId()){
	        		 key = APPELLATION;
	        	 }else if (spinnerId == varietalSpinner.getId()){
	        		 key = VARIETAL;
	        	 }
	        	 if (key!=null){
	        		// Look up the associated id for this selected item
	        		 int itemId = getIdForItem(key, position);
	        		 
	        		 selectedItems.put(key, itemId);
	        	 }
	         }

	         @Override
	         public void onNothingSelected(AdapterView<?> parentView) {
	            // do nothing
	         }
	      }); 
	}
	private int getIdForItem(String itemName, int index){
		int itemId = 0;
		List<Category> categories = app.getSearchCategories();
		for (Category category: categories){
			if (category.getName().equalsIgnoreCase(itemName)){
				Refinement item = category.getRefinements().get(index);
				itemId = item.getId().intValue();
				break;
			}
		}
		return itemId;
	}

	@Override
	public void onPreExecute() {
		// TODO Auto-generated method stub
		if (DEBUG) Log.i(TAG_TASK_FRAGMENT, "onPreExecute()");
	}

	@Override
	public void onProgressUpdate(int percent) {
		// TODO Auto-generated method stub
		if (DEBUG) Log.i(TAG_TASK_FRAGMENT, "onProgressUpdate()");
	}

	@Override
	public void onCancelled() {
		// TODO Auto-generated method stub
		if (DEBUG) Log.i(TAG_TASK_FRAGMENT, "onCancelled()");
		if (progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		
	}

	@Override
	public void onPostExecute() {
		if (DEBUG) Log.i(TAG_TASK_FRAGMENT, "onPostExecute()");
		if (progressDialog.isShowing()){
			progressDialog.cancel();
		}
		setUpDropdowns();
		
	}
}
