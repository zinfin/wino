package sandie.wino.strategy;

import sandie.wino.activities.MainActivity;
import android.content.Intent;
import android.net.Uri;

public class DownloadOptionsStrategy extends WineStrategy {

	public DownloadOptionsStrategy(MainActivity activity){
		super(activity);
	}
	@Override
	public void doRequest(Uri uri) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doRequest(Intent data) {
		// TODO Auto-generated method stub

	}
	@Override
	public void doRequest() {
		// TODO Auto-generated method stub

	}
	@Override
	public void doResult(Intent data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doError(Intent data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Intent makeIntent(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

}
