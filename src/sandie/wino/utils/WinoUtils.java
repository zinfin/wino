package sandie.wino.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class WinoUtils {
	/**
	 * Checks for a network connection
	 */
	public static boolean isConnected(ConnectivityManager connMgr){
		boolean isConnected = false;
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			isConnected = true;
		} 
		return isConnected;
	}
	/*
    * Set the result of the Activity to indicate whether the
    * operation on the content succeeded or not.
    * 
    * @param activity
    *          The Activity whose result is being set.
    * @param resultCode
    *          The result of the Activity, i.e., RESULT_CANCELED or
    *          RESULT_OK. 
    * @param failureReason
    *          String to add to add as an extra to the Intent passed
    *          back to the originating Activity if the result of the
    *          Activity is RESULT_CANCELED. 
    */
   public static void setActivityResult(Activity activity,
                                        int resultCode,
                                        String failureReason) {
       if (resultCode == Activity.RESULT_CANCELED)
           // Indicate why the operation on the content was
           // unsuccessful or was cancelled.
           activity.setResult(Activity.RESULT_CANCELED,
                new Intent("").putExtra("reason",
                                        failureReason));
       else 
           // Everything is ok.
           activity.setResult(Activity.RESULT_OK);
   }
}
