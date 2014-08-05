package sandie.wino.tasks;

import sandie.wino.WinoApp;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
	
	private ImageView imageView;
	private WinoApp _app;
	
	public ImageDownloadTask(Application application, ImageView imageView){
		_app = (WinoApp) application;
		this.imageView = imageView;
	}
	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bitmap = _app.retrieveBitmap(params[0]);
        return bitmap;
	}
	@Override 
	protected void onPostExecute(Bitmap bitmap){
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
			_app.getImageCache().put((Double) imageView.getTag(), bitmap);
	        imageView.setTag(null);
		}
	}

}
