package sandie.wino.fragment;

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
import sandie.wino.json.JsonWineParser;
import sandie.wino.model.Category;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
/**
 * GetSearchCriteriaFragment manages the task for getting the wine search
 * criteria and retains itself across
 * configuration changes.
 */
public class GetSearchCriteriaFragment extends Fragment {
	private static final String TAG = GetSearchCriteriaFragment.class.getSimpleName();
	private static final boolean DEBUG = true; // Set this to false to disable logs.
	private WinoApp _app;
	/**
	 * Callback interface through which the fragment can report the task's
	 * progress and results back to the Activity.
	 */
	public static interface TaskCallbacks {
		void onPreExecute();
		void onProgressUpdate(int percent);
		void onCancelled();
		void onPostExecute();
	}

	private TaskCallbacks mCallbacks;
	private LoadSearchCriteria mTask;
	private boolean mRunning;

	/**
	 * Hold a reference to the parent Activity so we can report the task's current
	 * progress and results. The Android framework will pass us a reference to the
	 * newly created Activity after each configuration change.
	 */
	@Override
	public void onAttach(Activity activity) {
		if (DEBUG) Log.i(TAG, "onAttach(Activity)");
		super.onAttach(activity);
		if (!(activity instanceof TaskCallbacks)) {
			throw new IllegalStateException("Activity must implement the TaskCallbacks interface.");
		}

		// Hold a reference to the parent Activity so we can report back the task's
		// current progress and results.
		mCallbacks = (TaskCallbacks) activity;
	}

	/**
	 * This method is called once when the Fragment is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (DEBUG) Log.i(TAG, "onCreate(Bundle)");
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	/**
	 * Note that this method is <em>not</em> called when the Fragment is being
	 * retained across Activity instances. It will, however, be called when its
	 * parent Activity is being destroyed for good (such as when the user clicks
	 * the back button, etc.).
	 */
	@Override
	public void onDestroy() {
		if (DEBUG) Log.i(TAG, "onDestroy()");
		super.onDestroy();
		cancel();
	}

	/*****************************/
	/***** TASK FRAGMENT API *****/
	/*****************************/

	/**
	 * Start the background task.
	 */
	public void start(Application app) {
		_app = (WinoApp) app;
		if (!mRunning) {
			mTask = new LoadSearchCriteria(_app);
			mTask.execute();
			mRunning = true;
		}
	}

	/**
	 * Cancel the background task.
	 */
	public void cancel() {
		if (mRunning) {
			mTask.cancel(false);
			mTask = null;
			mRunning = false;
		}
	}

	/**
	 * Returns the current state of the background task.
	 */
	public boolean isRunning() {
		return mRunning;
	}

	/***************************/
	/***** BACKGROUND TASKS ****/
	/***************************/

	/**
	 * An async task that retrieves the search options and proxies progress
	 * updates and results back to the Activity.
	 */
	private class LoadSearchCriteria extends AsyncTask<String, Void, List<Category>> {

		private static final String PARAM = "categorymap?filter=categories(490)&apikey=";
		private WinoApp _app;
		
		public LoadSearchCriteria(Application a){
			_app =(WinoApp) a;
		}
		@Override
		protected void onPreExecute() {
			// Proxy the call to the Activity.
		//	mCallbacks.onPreExecute();
			mRunning = true;
		}

		@Override
		protected void onCancelled() {
			// Proxy the call to the Activity.
			//mCallbacks.onCancelled();
			mRunning = false;
		}

		@Override
		protected void onPostExecute(List<Category> categories) {
			mRunning = false;
			_app.setSearchCategories(categories);
			Log.d(WineConstants.LOG_TAG, "Doing async post execute for loading search criteria...");
			// Proxy the call to the Activity
			mCallbacks.onPostExecute();
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
	}

	/************************/
	/***** LOGS & STUFF *****/
	/************************/

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (DEBUG) Log.i(TAG, "onActivityCreated(Bundle)");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		if (DEBUG) Log.i(TAG, "onStart()");
		super.onStart();
	}

	@Override
	public void onResume() {
		if (DEBUG) Log.i(TAG, "onResume()");
		super.onResume();
	}

	@Override
	public void onPause() {
		if (DEBUG) Log.i(TAG, "onPause()");
		super.onPause();
	}

	@Override
	public void onStop() {
		if (DEBUG) Log.i(TAG, "onStop()");
		super.onStop();
	}

}