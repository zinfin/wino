package sandie.wino.strategy;

import sandie.wino.activities.MainActivity;
import sandie.wino.activities.ShowSearchOptionsActivity;
import sandie.wino.strategy.DataStrategyManager.StrategyType;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class GetCategoryStrategy extends WineStrategy {

	public GetCategoryStrategy(MainActivity activity){
		super(activity);
	}
	@Override
	/**
	 * Run the strategy to download search options
	 */
	public void doRequest(Uri uri) {
		// TODO Make intent download search options, passing ordinal for strategy
		Intent intent = makeIntent(uri);
		try{
			mActivity.get().startActivityForResult(intent, StrategyType.DOWNLOAD_SEARCH_OPTIONS.ordinal());
		}catch (NullPointerException npe){
			Log.d(TAG, "Null pointer in doRequest()");
		}
	}

	public void doRequest(Intent data){
		
	}
	@Override
	public void doRequest() {
		// TODO Auto-generated method stub

	}
	@Override
	public void doResult(Intent data) {
		// TODO Auto-generated method stub, daisy chaining next strategy
		//mActivity.get().doRequest(StrategyType.DOWNLOAD_SEARCH_OPTIONS, data.getData());
	}

	@Override
	public void doError(Intent data) {
		// TODO Auto-generated method stub
		try{
			String reason = data.getStringExtra("reason");
			Toast.makeText(mActivity.get(), reason, Toast.LENGTH_SHORT).show();
			if (reason.equals("back button pressed")){
				mActivity.get().finish();
				Log.d(TAG, "finishing activity due to back button being pressed");
			}
		}catch(NullPointerException npe){
			Log.d(TAG, "Null pointeer exception in doError()");
			npe.printStackTrace();
		}
		
	}

	@Override
	protected Intent makeIntent(Uri uri) {
		// TODO Auto-generated method stub
		return new Intent(ShowSearchOptionsActivity.GET_SEARCH_CRITERIA);
	}

}
