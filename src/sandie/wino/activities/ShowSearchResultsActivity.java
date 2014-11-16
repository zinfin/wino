package sandie.wino.activities;

import java.util.List;

import sandie.wino.R;
import sandie.wino.WineListAdapter;
import sandie.wino.WinoApp;
import sandie.wino.fragment.RunSearchFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class ShowSearchResultsActivity extends Activity implements RunSearchFragment.TaskCallbacks{

		private WinoApp _app;
		private ProgressDialog progressDialog;
		private static final boolean DEBUG = true; 
		private static final String TAG_TASK_FRAGMENT = RunSearchFragment.class.getName();
		private RunSearchFragment mSearchFragment;



		@Override
		public void onCreate(Bundle savedInstanceState){
			if (DEBUG) Log.i(TAG_TASK_FRAGMENT, "onCreate()");
			super.onCreate(savedInstanceState);
			setContentView(R.layout.result_list);		
			
			_app = (WinoApp) getApplication();
			progressDialog = new ProgressDialog(this);
			progressDialog.setCancelable(false);
			progressDialog.setMessage(getString(R.string.searching));
			progressDialog.show();
			
			FragmentManager fm = getFragmentManager();
			mSearchFragment = (RunSearchFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
		    // If the Fragment is non-null, then it is currently being
		    // retained across a configuration change.
		    if (mSearchFragment == null) {
		    	mSearchFragment = new RunSearchFragment();
		      fm.beginTransaction().add(mSearchFragment, TAG_TASK_FRAGMENT).commit();
		    }
			
			if (mSearchFragment.isRunning()) {
				mSearchFragment.cancel();
				dismiss();
		    } else {
		    	mSearchFragment.start(_app);
		    }
			
			// Execute search task (old way)
			//new SearchWineTask(_app, this).execute();
			
		}
		private void dismiss(){
			System.out.println("Dismiss function called");
			if (progressDialog.isShowing()){
				progressDialog.dismiss();
			}
		}
		@Override 
		protected void onStop(){
			super.onStop();
			dismiss();
		}
		private void showSearchResults(){
			if (_app.getResults()!=null){
				List<sandie.wino.model.List> results = _app.getResults();
				for (sandie.wino.model.List item: results){
					System.out.println(item.getName());
				}

				WineListAdapter wineListAdapter =
						new WineListAdapter(this, R.layout.wine_search_results, results, _app);
				
				ListView lv = (ListView) findViewById(R.id.wine_list);
				lv.setAdapter(wineListAdapter);
			}
			// Clear out the selected Items
			_app.setSelectedItems(null);
		}
		@Override
		public void onPreExecute() {
			if (DEBUG) Log.i(TAG_TASK_FRAGMENT, "onPreExecute()");
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProgressUpdate(int percent) {
			if (DEBUG) Log.i(TAG_TASK_FRAGMENT, "onProgressUpdate()");
			
		}

		@Override
		public void onCancelled() {
			if (DEBUG) Log.i(TAG_TASK_FRAGMENT, "onCancelled()");
			dismiss();
			
		}
		/**
		 * When search task completes, this function is called
		 * and displays the search results.
		 */
		@Override
		public void onPostExecute() {
			if (DEBUG) Log.i(TAG_TASK_FRAGMENT, "onPostExecute()");
			// TODO Auto-generated method stub
			dismiss();
			showSearchResults();		
		}
}
