package sandie.wino.strategy;

import java.lang.ref.WeakReference;

import sandie.wino.activities.MainActivity;
import android.content.Intent;
import android.net.Uri;

public abstract class WineStrategy {

	/*
	 * Used by logger for debugging
	 */
	protected final String TAG = getClass().getName();
	protected final WeakReference<MainActivity> mActivity;
	
	WineStrategy(MainActivity activity){
			mActivity = new WeakReference<>(activity);
			
	}
	/*
	 * Performs the requested work for the given uri
	 */
	public abstract void doRequest(Uri uri);
	/*
	 * Performs the requested work given an intent
	 */
	public abstract void doRequest(Intent data);
	/*
	 * Performs the requested work
	 */
	public abstract void doRequest();
	/*
	 * Process the results from the request
	 */
	public abstract void doResult(Intent data);
	
	/*
	 * Handle any errors resulting from the request
	 */
	public abstract void doError(Intent data);
	/*
	 * Create an intent for this strategy
	 */
	protected abstract Intent makeIntent(Uri uri);
	
}
