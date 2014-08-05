package sandie.wino;

import java.util.List;

import sandie.wino.tasks.ImageDownloadTask;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WineListAdapter extends ArrayAdapter<sandie.wino.model.List> {
	Context context;
	WinoApp _app;
	public WineListAdapter(Context context, int resource,
			List<sandie.wino.model.List> objects, Application app) {
		super(context, resource, objects);
		this.context = context;
		_app = (WinoApp) app;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.wine_search_results, parent,false);
		}
        // use ViewHolder here to prevent multiple calls to findViewById (if you have a large collection)
        TextView text = (TextView) convertView.findViewById(R.id.wine_name);
        ImageView image = (ImageView) convertView.findViewById(R.id.wine_img);

        image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),0));
        sandie.wino.model.List item = getItem(position);
        if (item != null) {
            text.setText(item.getName());
            Bitmap bitmap = _app.getImageCache().get(item.getId());
            if (bitmap != null) {
               image.setImageBitmap(bitmap);
            } else {
               // put item ID on image as TAG for use in task
               image.setTag(item.getId());
               // separate thread/via task, for retrieving each image
               // (note that this is brittle as is, should stop all threads in onPause)               
               new ImageDownloadTask(_app, image).execute(item.getLabels().get(0).getUrl());
            }
         }

         return convertView;
	}

}
