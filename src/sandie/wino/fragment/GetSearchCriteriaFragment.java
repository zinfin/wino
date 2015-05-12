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
		super.onAttach(activity);
		// Hold a reference to the parent Activity so we can report back the task's
		// current progress and results.
		mCallbacks = (TaskCallbacks) activity;
        _app = (WinoApp) activity.getApplication();
	}

	/**
	 * This method is called once when the Fragment is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
        /* Create and execute the background task */
        mTask = new LoadSearchCriteria(_app);
        mTask.execute();
	}

    /**
     * Set the callback to null so we don't accidentally leak the Activity
     * instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
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
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
		}

		@Override
		protected void onCancelled() {
			// Proxy the call to the Activity.
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }
		}

		@Override
		protected void onPostExecute(List<Category> categories) {
			_app.setSearchCategories(categories);
			// Proxy the call to the Activity
            if (mCallbacks != null) {
                mCallbacks.onPostExecute();
            }
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
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

}