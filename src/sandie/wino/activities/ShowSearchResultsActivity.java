package sandie.wino.activities;

import java.util.List;

import sandie.wino.R;
import sandie.wino.WineListAdapter;
import sandie.wino.WinoApp;
import sandie.wino.interfaces.OnTaskCompleted;
import sandie.wino.tasks.SearchWineTask;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;

public class ShowSearchResultsActivity extends ListActivity implements OnTaskCompleted{

		private WinoApp _app;
		private ProgressDialog progressDialog;
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.result_list);		
			
			_app = (WinoApp) getApplication();
			progressDialog = new ProgressDialog(this);
			progressDialog.setCancelable(false);
			progressDialog.setMessage(getString(R.string.searching));
			progressDialog.show();
			
			// Execute search task
			new SearchWineTask(_app, this).execute();
			
		}

		@Override
		/**
		 * When search task completes, this function is called
		 * and displays the search results.
		 */
		public void callback() {
			if (progressDialog.isShowing()){
				progressDialog.cancel();
			}
			if (_app.getResults()!=null){
				List<sandie.wino.model.List> results = _app.getResults();
				for (sandie.wino.model.List item: results){
					System.out.println(item.getName());
				}

				WineListAdapter wineListAdapter =
						new WineListAdapter(this, R.layout.wine_search_results, results, _app);
				
				setListAdapter(wineListAdapter);
			}
			// Clear out the selected Items
			_app.setSelectedItems(null);
		}
}
