package sandie.wino.activities;

import sandie.wino.fragment.RetainedFragmentManager;
import sandie.wino.utils.WinoUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A generic activity that can be customized to perform some type of download operation, 
 * returning content as the result of the Activity. AsyncTask framework is used to
 * perform operation in a background thread.
 * @author sandie
 *
 * @param <ReturnType>
 */
public abstract class GenericBackgroundActivity<ReturnType> extends
		LifecycleLoggingActivity {
	/*
	 * Display progress bar
	 */
	protected ProgressBar mLoadingProgressBar;
	/*
	 * A text view used with progress bar
	 */
	protected TextView mTextView;
	/*
	 * Activity name
	 */
	protected final String mActivityName = getClass().getSimpleName();
	/*
	 * Keep track of state between config changes
	 */
	protected final RetainedFragmentManager mRetainedFragmentManager =
			new RetainedFragmentManager(this.getFragmentManager(), mActivityName);
	
	/* Constants used to store data */
	private final static String URL = "url";
	private final static String CONTENT = "content";
	private final static String ASYNC_TASK = "async_task";
	 /*
	  * Initialize layout and class scope variables
	  */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// Set up layout
		// Store progress bar
		//mLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar_loading);
		// Store textview from progress bar
		//mTextview = (TextView) findViewById(R.id.textView1);
	}
	/**
	 * Called after onCreate() or after onRestart().  Re-acquire
	 * resources released when activity was stopped or acquire
	 * resources for first time after onCreate()
	 */
	@Override
	protected void onStart(){
		super.onStart();
		// Handle configuration changes, if any
		handleConfigurationChanges();
		// Show progress bar
		mLoadingProgressBar.setVisibility(View.VISIBLE);
		
		GenericAsyncTask asyncTask = mRetainedFragmentManager.get(ASYNC_TASK);
		
		if (asyncTask == null ){
			Log.d(TAG, "No running task, creating one for first time");
			final Uri url = mRetainedFragmentManager.get(URL);
			mRetainedFragmentManager.put(ASYNC_TASK,
					new GenericAsyncTask(mRetainedFragmentManager, mActivityName)
					.execute(url));
		}else{
			Log.d(TAG, "onStart() not creating new AsyncTask");			
		}
	}
	/**
	 * When activity is no longer visible, hide progress bar.
	 */
	@Override
	protected void onStop(){
		super.onStop();
		Log.d(mActivityName, "onStop() making progress bar invisible");
		// Dismiss progress bar
		mLoadingProgressBar.setVisibility(View.INVISIBLE);
		
	}
	/**
	 * When back button is pressed, handle some state and task checking
	 */
	@Override
	public void onBackPressed(){
		//Log
		Log.d(TAG,"Back button pressed");
		//Find the AsyncTask if it exists, cancel it and set to null
		GenericAsyncTask asyncTask = mRetainedFragmentManager.get(ASYNC_TASK);
		if (asyncTask !=null){
			Log.d(TAG,"Cancelling task");
			
			//Cancel task
			asyncTask.cancel(true);
			
			// Set to null
			mRetainedFragmentManager.put(ASYNC_TASK, null);			
		}
		// Hide progress bar
		mLoadingProgressBar.setVisibility(View.INVISIBLE);
		
		// Handle activity's result
		WinoUtils.setActivityResult(this, 0, "back button pressed");
		
		// Call super class method
		super.onBackPressed();
	}
	/**
	 * Handle reconfiguration changes
	 */
	protected void handleConfigurationChanges(){
		// Have we returned from a configuration change?
		if (mRetainedFragmentManager.firstTimeIn()){
			// Store the request info in retained frag manager
			mRetainedFragmentManager.put(URL, getIntent().getData());
			Log.d(TAG, "first time onCreate() " + mRetainedFragmentManager.get(URL));
		}else{
			// RetainedFragmentManager has already been initialized
			// indicating a config change occurred.
			Log.d(TAG,"second time onCreate() " + mRetainedFragmentManager.get(URL));
			
			ReturnType content = mRetainedFragmentManager.get(CONTENT);
			// If content is not null, then we've completed task so we set the result
			// of activity and finish it
			if (content != null){
				Log.d(TAG,"Finish activity since result has been returned");
				 // Call back the Activity's hook method which returns true
				// if finish() should be called, else false
				if (onPostExecute(content)){
					// The finish method stops the activity from
					//running should be called in the UI thread
					finish();
				}else{
					Log.d(TAG, "continuing with task since we haven't completed...");
				}
			}
		}
	}
	/**
	 * Override this method to define behavior that will run in a background thread
	 */
	abstract protected ReturnType onExecute(Uri url);
	
	/**
	 * Override this method to define behavior that runs after the onExecute() method completes
	 * in a background thread.  By default it should return true which will trigger the 
	 * finish() method to be called on the activity.
	 */
	protected boolean onPostExecute(ReturnType content){ return true ; }
	
	/**
	 * GenericTask customized by subclasses of GenericBackgroundActivity to 
	 * handle requests to obtain data from web services
	 */
	protected class GenericAsyncTask extends AsyncTask<Uri, Void, ReturnType>{

		/*
		 * Debug TAG used by logger
		 */
		protected String TAG = getClass().getSimpleName();
		
		/* 
		 * Store state information between configuration changes
		 */
		protected final RetainedFragmentManager mRetainedFragmentManager;
		
		GenericAsyncTask (RetainedFragmentManager retainedFragManager, String activityName){
			mRetainedFragmentManager = retainedFragManager;
			TAG = TAG + activityName;
			
		}
		/**
		 * Behavior and state to apply before task is executed
		 */
		@Override
		protected void onPreExecute(){
			Log.d(TAG,"in onPreExecute");
			// Nothing happening here now...
		}
		/* 
		 * Dispatch task to perform web service call
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected ReturnType doInBackground(Uri... params) {
			Log.d(TAG, "in doInBackground");
			
			try {
				// Call doExecute() method to perform specific call
				return onExecute(params[0]);
			} catch (NullPointerException npe) {
				// TODO Auto-generated catch block
				npe.printStackTrace();
				return null;
			}
		}
		
		/**
		 * After background task completes, perform any post-processing and
		 * set result of activity and finish activity
		 */
		@Override
		protected void onPostExecute(ReturnType content){
			Log.d(TAG, "in onPostExecute");
			
			// Update content of fragment
			mRetainedFragmentManager.put(CONTENT, content);
			
			// Get the attached activity
			@SuppressWarnings("unchecked")
			GenericBackgroundActivity<ReturnType> activity =
				(GenericBackgroundActivity<ReturnType>) mRetainedFragmentManager.getActivity();
			
			// Make activity exists before proceeding
			if (activity !=null){
				// Call the onPostExecute method in the Activity
				if(activity.onPostExecute(content)){
					activity.fileList();
					
					// Set asysnc task to null
					mRetainedFragmentManager.put(ASYNC_TASK, null);
				}else{
					Log.d(TAG, "*** Activity is null onPostExecute ***");
				}
			}
		}
		
	}
	
}
