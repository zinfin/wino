package sandie.wino.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class WinoUtils {
	
	/** Some constants used for HTTP communication */
	private static final int TIMEOUT = 15000;
	private static final String GET = "GET";
	private static final String CONTENT_LENGTH_STRING = "Content-length";
	private static final String CONTENT_LENGTH = "0";
	
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
	/**
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
   /**
    * Return an InputStream from a GET request given a valid url.
    * @param url
    * @return InputStream
    */
   public static InputStream getJSONSTream (String url) {
	   HttpURLConnection urlConnection = null;
	   try{
		   URL u = new URL(url);
		   urlConnection = (HttpURLConnection) u.openConnection();
		   urlConnection.setRequestMethod(GET);
		   urlConnection.setRequestProperty(CONTENT_LENGTH_STRING, CONTENT_LENGTH);
		   urlConnection.setUseCaches(false);
		   urlConnection.setAllowUserInteraction(false);
		   urlConnection.setConnectTimeout(TIMEOUT);
		   urlConnection.connect();
		   int status = urlConnection.getResponseCode();
		   
		   switch (status){
		   		case 200:
		   		case 201:
//		   			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//		   			StringBuilder sb = new StringBuilder();
//		   			String line;
//		   			while (br.readLine()!=null){
//		   				line = br.readLine();
//		   				sb.append(line+"\n");
//		   			}
//		   			br.close();
		   			return urlConnection.getInputStream();
		
		   }
		   
	   }catch (MalformedURLException mex){
		   Log.d("WinoUtils", "Malformed URL Exception: " + mex.getMessage());
		   mex.printStackTrace();
		   
	   }catch (IOException io){
		   Log.d("WinoUtils", "IO Exception "+ io.getMessage());  
		   io.printStackTrace();
		   
	   }finally{
		   if (urlConnection !=null){
			   try{
				   urlConnection.disconnect();
			   }catch (Exception e){
				   Log.d("WinoUtils", "Exception in finally block");
				   e.printStackTrace();
			   }
		   }
		  
	   }
	return null;
	   
   }

}
