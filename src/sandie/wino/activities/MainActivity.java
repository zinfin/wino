package sandie.wino.activities;

import sandie.wino.strategy.DataStrategyManager;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends LifecycleLoggingActivity {
	/**
	 * A class that manages the strategy objects responsible for particular data
	 * retrieval operations
	 */
	private final DataStrategyManager mDataStrategyManager = new DataStrategyManager(this);
	
	/**
	 * When activity is first created, some initialization occurs as well as setting up the ui
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Kick off the first activity which load search options
		doRequest(DataStrategyManager.StrategyType.DOWNLOAD_SEARCH_OPTIONS);
	}
	
	/**
	 * Handle the result of an activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		doResult(requestCode, resultCode, data);
	}
	/**
	 * Launch specific Activty related to this strategy
	 * @param strategyType
	 */
	public void doRequest(DataStrategyManager.StrategyType strategyType){
		mDataStrategyManager.doRequest(strategyType);
	}
	/**
	 * Handle specific result related to this strategy
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void doResult(int requestCode, int resultCode, Intent data){
		mDataStrategyManager.doResult(requestCode, resultCode, data);
	}
}
