package pl.soad.alzheimer_gps;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class GpsActivity extends Activity implements LocationListener {

	TextView t1, t2, t3, t4;
	EditText e1, e2;
	LocationManager locationManager;
	Location location;
	Criteria criteria;
	String bestProvider;
	private double r = 2, rEarth = 6371; // km
	private double longitude, latitude, longitudeChecked, latitudeChecked, longitudeMargin, latitudeMargin;
	private double dLatitude, dLongitude, tmp1, tmp2, distance;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		
		t1 = (TextView) findViewById(R.id.gps_TextView1);
		t2 = (TextView) findViewById(R.id.gps_TextView2);
		t3 = (TextView) findViewById(R.id.gps_TextView3);
		t4 = (TextView) findViewById(R.id.gps_TextView4);
		e1 = (EditText) findViewById(R.id.gps_editText1);
		e2 = (EditText) findViewById(R.id.gps_editText2);
		
		criteria = new Criteria();
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		refresh();
		/* 
		 * requestLocationUpdates(provider, minTime, minDist, listener)
		 * */
		locationManager.requestLocationUpdates(bestProvider, 50000, 10, this);
		
		t1.setText("najlepszy dostawca: " + bestProvider);
		t2.setText("longitude: " + location.getLongitude());
		t3.setText("latitude: " + location.getLatitude());
		t3.setText("historia\n");
		e1.setText("50.523493");
		e2.setText("19.432543");
		
	}

	
	private void refresh(){
		bestProvider = locationManager.getBestProvider(criteria, true);
		location = locationManager.getLastKnownLocation(bestProvider);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		refresh();
		
		latitude = location.getLatitude();
		//latitude = 111 * latitude;
		longitude = location.getLongitude();
		//longitude *= 111.32 * Math.cos(longitude);
		
		t1.setText("najlepszy dostawca: " + bestProvider);
		t2.setText("longitude: " + longitude);
		t3.setText("latitude: " + latitude);
		t4.setText(t4.getText() + "" + longitude + " / " + latitude + "\n");
		
		longitudeChecked = Double.parseDouble(e1.getText().toString());
		latitudeChecked = Double.parseDouble(e2.getText().toString());
		
		dLatitude = latitude - latitudeChecked;
		dLongitude = longitude - longitudeChecked;
		
		latitude = Math.toRadians(latitude);
		longitude = Math.toRadians(longitude);
		latitudeChecked = Math.toRadians(latitudeChecked);
		longitudeChecked = Math.toRadians(longitudeChecked);
		dLatitude = Math.toRadians(dLatitude);
		dLongitude = Math.toRadians(dLongitude);
		
		tmp1 = Math.pow(Math.sin(dLatitude/2), 2) + Math.cos(latitude) + Math.cos(latitudeChecked) +
			   Math.pow(Math.sin(dLongitude/2), 2);
		tmp2 = 2 * Math.atan2(Math.sqrt(tmp1), Math.sqrt(1-tmp1));
		distance = rEarth * tmp2;
		
		
		
		//longitudeMargin = longitude - longitudeChecked;
		//latitudeMargin = latitude - latitudeChecked;
		
		if (distance > r || distance > r){
			// send sms;
		}
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}
