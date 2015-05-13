package sandie.wino.activities;

import java.util.List;

import sandie.wino.R;
import sandie.wino.WineListAdapter;
import sandie.wino.WinoApp;
import sandie.wino.fragment.RunSearchFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

public class ShowSearchResultsActivity extends LifecycleLoggingActivity implements RunSearchFragment.TaskCallbacks{

		private WinoApp _app;
		private ProgressDialog progressDialog;
		private static final String TAG_TASK_FRAGMENT = RunSearchFragment.class.getName();
		private RunSearchFragment mSearchFragment;

		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.result_list);		
			
			_app = (WinoApp) getApplication();

			if (_app.getSelectedItems()!=null){
                FragmentManager fm = getFragmentManager();
                mSearchFragment = (RunSearchFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
                // If the Fragment is non-null, then it is currently being
                // retained across a configuration change.
                if (mSearchFragment == null) {
                    mSearchFragment = new RunSearchFragment();
                    fm.beginTransaction().add(mSearchFragment, TAG_TASK_FRAGMENT).commit();
                }
            }


			
		}
        /*
        @Override
        public void onResume(){
            Log.i(TAG_TASK_FRAGMENT, "ShowSearchResultsActivity: ON RESUME");
        }*/
        private void dismissProgressDialog(){
            if (progressDialog!=null && progressDialog.isShowing()){
                progressDialog.cancel();
            }
        }
		@Override
		protected void onStop(){
			super.onStop();
			dismissProgressDialog();
		}
		private void showSearchResults(){
			if (_app.getResults()!=null){
				List<sandie.wino.model.List> results = _app.getResults();
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
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.searching));
            progressDialog.show();
			
		}

		@Override
		public void onProgressUpdate(int percent) {
			
		}

		@Override
		public void onCancelled() {
			dismissProgressDialog();
		}
		/**
		 * When search task completes, this function is called
		 * and displays the search results.
		 */
		@Override
		public void onPostExecute() {
			dismissProgressDialog();
			showSearchResults();
		}
}
