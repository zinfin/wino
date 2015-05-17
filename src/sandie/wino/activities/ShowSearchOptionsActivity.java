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
import sandie.wino.utils.WinoUtils;
import sandie.wino.view.NoDefaultSpinner;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class ShowSearchOptionsActivity extends LifecycleLoggingActivity implements GetSearchCriteriaFragment.TaskCallbacks{
	private static final AbstractHttpClient httpClient;

	private static final String TAG_TASK_FRAGMENT = GetSearchCriteriaFragment.class.getName() ;
    private static final String CRITERIA_FRAGMENT = "criteria fragment";
	
	private NoDefaultSpinner wineTypeSpinner, varietalSpinner, wineStyleSpinner,
	regionSpinner,vintageSpinner,foodSpinner, appellationSpinner;

	private Button searchBtn, resetBtn;
	
	private ProgressDialog progressDialog;
	private WinoApp app;
	private Map<String,Integer> selectedItems;
    private FragmentManager mFragManager;
    private GetSearchCriteriaFragment mCriteriaFragment;
    public static String GET_SEARCH_CRITERIA = "android.intent.action.PROMPT_USER";
	
	private static final HttpRequestRetryHandler retryHandler;

   private final String TAG = getClass().getSimpleName();
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
		// Start by showing the search options
		setContentView(R.layout.activity_show_search);

        // Set up progress dialogs showing a message that
        // data is loading while the search filters are loading
        mFragManager = getFragmentManager();
		
		// Use Application object for app wide state
		app = (WinoApp) getApplication();

        // Restore any bundle state
        if (null != savedInstanceState) {
            restoreState(savedInstanceState);
        } else {
            setUpFragments();
        }
        ConnectivityManager connMgr = (ConnectivityManager) 
		        getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!WinoUtils.isConnected(connMgr)){
        	Toast.makeText(getApplicationContext(), R.string.show_search_activity_network_unavailable, Toast.LENGTH_SHORT).show();;
        }else{
        	setUpDropdowns();
			if (progressDialog.isShowing()){
				progressDialog.cancel();
			}
			// Add click listener to search button
			searchBtn = (Button) findViewById(R.id.searchWineBtn);
			searchBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					if (selectedItems !=null){
	                    // Clear any results
	                    app.clearResultList();
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


		
	}
    private void setupProgressDialogs(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.show_search_activity_retrieving_data));
        progressDialog.show();
    }
    @Override
    protected void onStop(){
        super.onStop();
        dismissProgressDialog();
    }

    /**
     * Download search options
     */
    public void downloadOptions(){
    	// TODO surround with try catch
    	// ImageUtils.setActivityResult(this, ....);
    	// shutdown activity  finish();
    }
    /**
     * Notify user back button was pressed
     */
	@Override
	public void onBackPressed(){
		Log.d(TAG, "back button pressed");
		// Handle in activity result handler
		// ImageUtils.setActivityResult(this, null, "back button pressed");
		super.onBackPressed();
	}

	/**
	 * Set up drop downs for view
	 */
	public void setUpDropdowns(){
        dismissProgressDialog();
        final String wType = getString(R.string.wine_type);
        final String wVarietal = getString(R.string.wine_vartietal);
        final String wStyle = getString(R.string.wine_style);
        final String wRegion = getString(R.string.wine_region);
        final String wVintage = getString(R.string.wine_vintage);
        final String wFood = getString(R.string.wine_food);
        final String wAppellation = getString(R.string.wine_appellation);

		if ((app.getSearchCategories() != null) & !app.getSearchCategories().isEmpty()){
			
			// Extract specific categories and populate appropriate spinners with refinements
			for (Category category : app.getSearchCategories()){
				List<Refinement> items = category.getRefinements();
				
				if (category.getName().equalsIgnoreCase(wType)){
					wineTypeSpinner=(NoDefaultSpinner) findViewById(R.id.wine_type_spinner);
					assignSpinner(wineTypeSpinner, items);
				}
				if (category.getName().equalsIgnoreCase(wVarietal)){
					varietalSpinner=(NoDefaultSpinner) findViewById(R.id.varietal_spinner);
					assignSpinner(varietalSpinner, items);
				}
				if (category.getName().equalsIgnoreCase(wStyle)){
					wineStyleSpinner=(NoDefaultSpinner) findViewById(R.id.wine_style_spinner);
					assignSpinner(wineStyleSpinner, items);
				}	
				if (category.getName().equalsIgnoreCase(wRegion)){
					regionSpinner=(NoDefaultSpinner) findViewById(R.id.region_spinner);
					assignSpinner(regionSpinner, items);
				}
				if (category.getName().equalsIgnoreCase(wVintage)){
					vintageSpinner=(NoDefaultSpinner) findViewById(R.id.vintage_spinner);
					assignSpinner(vintageSpinner, items);
				}
				if (category.getName().equalsIgnoreCase(wFood)){
					foodSpinner=(NoDefaultSpinner) findViewById(R.id.food_type_spinner);
					assignSpinner(foodSpinner, items);
				}
				if (category.getName().equalsIgnoreCase(wAppellation)){
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
	        		 key = getString(R.string.wine_style);
	        		 
	        	 }if (spinnerId == wineTypeSpinner.getId()) {
	        		 key = getString(R.string.wine_type);
	        	 }else if(spinnerId == regionSpinner.getId()){
	        		 key = getString(R.string.wine_region);
	        	 }else if (spinnerId == vintageSpinner.getId()){
	        		 key = getString(R.string.wine_vintage);
	        	 }else if (spinnerId == foodSpinner.getId()){
	        		 key = getString(R.string.wine_food);
	        	 }else if (spinnerId == appellationSpinner.getId()){
	        		 key = getString(R.string.wine_appellation);
	        	 }else if (spinnerId == varietalSpinner.getId()){
	        		 key = getString(R.string.wine_vartietal);
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
	}

	@Override
	public void onProgressUpdate(int percent) {
	}

	@Override
	public void onCancelled() {
		dismissProgressDialog();
	}

	@Override
	public void onPostExecute() {
		dismissProgressDialog();
		setUpDropdowns();
	}
    private void dismissProgressDialog(){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if (null != mCriteriaFragment){
            savedInstanceState.putString(CRITERIA_FRAGMENT,
                    mCriteriaFragment.getTag());
        }
        super.onSaveInstanceState(savedInstanceState);
    }
    private void restoreState(Bundle savedInstanceState){
        //Fragments tags were saved in onSavedInstanceState
        mCriteriaFragment = (GetSearchCriteriaFragment) mFragManager
                .findFragmentByTag(savedInstanceState
                        .getString(CRITERIA_FRAGMENT));
    }
    // One time setup of UI and retained (headless) Fragment
    private void setUpFragments(){
        mCriteriaFragment = new GetSearchCriteriaFragment();
        mFragManager.beginTransaction().add(mCriteriaFragment, TAG_TASK_FRAGMENT).commit();
    }
}
