package sandie.wino.strategy;

import sandie.wino.activities.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class DataStrategyManager {
	/*
	 * Debugging tag
	 */
	protected final String TAG = getClass().getSimpleName();
	
	/*
	 * Strategies handled by this manager
	 */
	public enum StrategyType{
		/*
		 * Each of these uniquely maps to specific strategies used
		 * to perform various activities
		 */
		DOWNLOAD_SEARCH_OPTIONS,
		PERFORM_SEARCH,
		GET_DETAIL
	}
	
	/*
	 * Array mapping StrategyType to DataStrategy
	 */
	private WineStrategy mStrategies[] = new WineStrategy[StrategyType.values().length];
	
	public DataStrategyManager(MainActivity activity) {
		// Load up the strategies
		mStrategies[StrategyType.DOWNLOAD_SEARCH_OPTIONS.ordinal()]= new DownloadOptionsStrategy(activity);
		
	}
	/**
	 * Perform strategy associated with strategy type and a uri
	 */
	public void doRequest(StrategyType strategyType, Uri uri){
		mStrategies[strategyType.ordinal()].doRequest(uri);
	}
	/**
	 * Perform strategy associated with strategy type and an Intent
	 */
	public void doRequest(StrategyType strategyType, Intent data){
		mStrategies[strategyType.ordinal()].doRequest(data);
	}
	/**
	 * Perform strategy associated with strategy type
	 */
	public void doRequest(StrategyType strategyType){
		mStrategies[strategyType.ordinal()].doRequest();
	}
	/**
	 * Process result specific to strategy
	 */
	public void doResult(int requestCode, int resultCode, Intent data){
		// Determine if result was successful and handle accordingly
		if (resultCode == Activity.RESULT_OK){
			// Call the appropriate doResult method for the strategy based on the request code
			mStrategies[requestCode].doRequest(data);
		}else if (resultCode == Activity.RESULT_CANCELED){
			mStrategies[requestCode].doError(data);
		}
	}
}
