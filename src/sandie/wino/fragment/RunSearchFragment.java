package sandie.wino.fragment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.client.HttpClient;

import sandie.wino.WineConstants;
import sandie.wino.WineSearchFactory;
import sandie.wino.WinoApp;
import sandie.wino.activities.ShowSearchOptionsActivity;
import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
/**
 * RunSearchFragment manages the task of searching for wines given
 * a set of search criteria.
 * @author sandie
 *
 */
public class RunSearchFragment extends Fragment {
	private static final String TAG = RunSearchFragment.class.getSimpleName();
	private static final boolean DEBUG = true; 
	private WinoApp _app;

	private TaskCallbacks mCallbacks;
	private SearchForWines mTask;
	private boolean mRunning;
	
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
		if (DEBUG) Log.i(TAG, "onCreate()");
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
			mTask = new SearchForWines(_app);
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
	/**
	 * An async task that returns a list of wines given criteria stored in the Wino app object
	 * @author sandie
	 *
	 */
	private class SearchForWines extends AsyncTask<Object, Void, java.util.List<sandie.wino.model.List>> {
		private WinoApp _app;
		private int _resultSize;
		private int _offset;
		
		public SearchForWines(Application application){
			_app = (WinoApp) application;
		}
		@Override
		protected java.util.List<sandie.wino.model.List> doInBackground(Object... params) {
			// Get selected items from application
			HashMap<String,Integer> searchMap = (HashMap<String, Integer>) _app.getSelectedItems();
			// Pull out the values
			Collection<Integer> ids =searchMap.values();
			Iterator<Integer> iter = ids.iterator();
			int [] wineIds = new int[ids.size()];
			int z=0;
			while (iter.hasNext()){
				wineIds[z] = iter.next();
				z = z + 1;
			}
			_resultSize = _app.getResultSize();
			_offset = _app.getOffset();
			final HttpClient httpClient = ShowSearchOptionsActivity.getHttpClient();
			return WineSearchFactory.searchByCategories(httpClient, wineIds, _resultSize, _offset);
		}
		@Override
		protected void onPostExecute(java.util.List<sandie.wino.model.List> results){
			_app.setResults(results);
			Log.d(WineConstants.LOG_TAG, "Search fragment post execute...");
			// Proxy the call to the Activity
			mCallbacks.onPostExecute();
					
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
