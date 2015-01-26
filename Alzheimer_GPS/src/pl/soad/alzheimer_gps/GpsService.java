package pl.soad.alzheimer_gps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GpsService extends Service{

	private static final String TAG = "GpsService";
	GpsActivity gps;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Gps Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
		gps = new GpsActivity();
		gps.startGettingLocation();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Gps Service Stopped", Toast.LENGTH_LONG).show();
		gps.stopUsingGps();
		Log.d(TAG, "onDestroy");
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "Gps Service Started", Toast.LENGTH_LONG).show();
		gps.getLocation();
		Log.d(TAG, "onStart");
	}
}
