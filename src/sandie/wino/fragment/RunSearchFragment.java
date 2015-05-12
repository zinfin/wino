package sandie.wino.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.client.HttpClient;

import sandie.wino.R;
import sandie.wino.WineConstants;
import sandie.wino.WineListAdapter;
import sandie.wino.WineSearchFactory;
import sandie.wino.WinoApp;
import sandie.wino.activities.ShowSearchOptionsActivity;
import sandie.wino.model.List;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.widget.ListView;

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
		super.onAttach(activity);
        Log.i(WineConstants.LOG_TAG, "RunSearchFragment: ON ATTACH");
		// Hold a reference to the parent Activity so we can report back the task's
		// current progress and results.
		mCallbacks = (TaskCallbacks) activity;
        mCallbacks.onPreExecute();
        _app = (WinoApp) activity.getApplication();

        if(_app.getResults()!=null && _app.getResults().size()>0){
            mCallbacks.onPostExecute();
        }

	}

	/**
	 * This method is called once when the Fragment is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        Log.i(WineConstants.LOG_TAG, "RunSearchFragment: ON CREATE");
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
        mTask = new SearchForWines(_app);
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

	/*****************************/
	/***** TASK FRAGMENT API *****/
	/*****************************/

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
            Log.i(WineConstants.LOG_TAG, "SearchForWinesTask: DO IN BACKGROUND");

            java.util.List<sandie.wino.model.List> items = new ArrayList<List>( );
			// Get selected items from application
            if ( _app.getSelectedItems()!=null  ){
			    HashMap<String,Integer> searchMap = (HashMap<String, Integer>) _app.getSelectedItems();

                // Pull out the values
                Collection<Integer> ids =searchMap.values();
                Iterator<Integer> iter = ids.iterator();
                int [] wineIds = new int[ids.size()];
                Log.i(WineConstants.LOG_TAG, "Count of items to search for: " + wineIds.length);
                int z=0;
                while (iter.hasNext()){
                    wineIds[z] = iter.next();
                    z = z + 1;
                }
                _resultSize = _app.getResultSize();
                _offset = _app.getOffset();
                final HttpClient httpClient = ShowSearchOptionsActivity.getHttpClient();
                items =  WineSearchFactory.searchByCategories(httpClient, wineIds, _resultSize, _offset);
            }

            return items;
		}
		@Override
		protected void onPostExecute(java.util.List<sandie.wino.model.List> results){
			_app.setResults(results);
			Log.d(WineConstants.LOG_TAG, "SearchForWinesTask: ON POST EXECUTE");
			// Proxy the call to the Activity
            if(mCallbacks!=null){
                mCallbacks.onPostExecute();
            }

		}
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {

        super.onStart();
        Log.i(WineConstants.LOG_TAG, "RunSearchFragment: ON START");
	}

	@Override
	public void onResume() {
		super.onResume();
        Log.i(WineConstants.LOG_TAG, "RunSearchFragment: ON RESUME");
	}

	@Override
	public void onPause() {

        super.onPause();
        Log.i(WineConstants.LOG_TAG, "RunSearchFragment: ON RESUME");
	}

	@Override
	public void onStop() {
		super.onStop();
	}

}
