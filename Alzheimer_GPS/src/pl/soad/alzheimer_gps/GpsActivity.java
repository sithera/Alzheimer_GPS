package pl.soad.alzheimer_gps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
<<<<<<< HEAD
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
=======
import android.location.Criteria;
>>>>>>> 36ae6a053960446e53f49e98ccadaeb204c5de5a
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GpsActivity extends Activity implements LocationListener{

	TextView t1, t2, t3, t4;
	EditText e1, e2;
	Button b1;
	LocationManager locationManager;
	Location location;
	String bestProvider = "";
	Geocoder geocoder;
	AlarmManager alarmManager;
	Intent intent;
	PendingIntent pendingIntent;
	private double r = 2; // km
	private double longitude, latitude, longitudeChecked, latitudeChecked, 
	dLatitude, dLongitude, distance;
	Context context = GpsActivity.this;
	boolean canGetLocation = false;
	boolean isGpsEnabled = false;
	boolean isNetworkEnabled = false;

	private static final long MIN_TIME = 1000 * 10 * 1; // 10 seconds
	private static final long MIN_DISTANCE = 1000; // meters
	
=======
import android.widget.EditText;
import android.widget.TextView;

public class GpsActivity extends Activity implements LocationListener {
>>>>>>> 36ae6a053960446e53f49e98ccadaeb204c5de5a

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
<<<<<<< HEAD

=======
		
>>>>>>> 36ae6a053960446e53f49e98ccadaeb204c5de5a
		t1 = (TextView) findViewById(R.id.gps_TextView1);
		t2 = (TextView) findViewById(R.id.gps_TextView2);
		t3 = (TextView) findViewById(R.id.gps_TextView3);
		t4 = (TextView) findViewById(R.id.gps_TextView4);
		e1 = (EditText) findViewById(R.id.gps_editText1);
		e2 = (EditText) findViewById(R.id.gps_editText2);
<<<<<<< HEAD
		b1 = (Button) 	findViewById(R.id.gps_btn1);

		geocoder = new Geocoder(this, Locale.getDefault());
		
		t4.setText("historia\n");
		e1.setText("50.523493");
		e2.setText("19.432543");
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getLocation();
				updateTextViews();
				checkLocation();
				translateLongitude();
				
			}

		});
=======
		
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
		
>>>>>>> 36ae6a053960446e53f49e98ccadaeb204c5de5a
	}
		
	@Override
	public void onLocationChanged(Location location) {
			}

	public void checkLocation(){
		
		longitudeChecked = Double.parseDouble(e1.getText().toString());
		latitudeChecked = Double.parseDouble(e2.getText().toString());

		longitude = longitude * 111.32;
		longitude = longitude * Math.cos(latitude);
		
		latitude *= 110.54;
		longitudeChecked = longitudeChecked * 111.32 * Math.cos(latitudeChecked);
		latitudeChecked *= 110.54;

		dLatitude = latitude - latitudeChecked;
		dLongitude = longitude - longitudeChecked;

		Log.d("TAG", String.valueOf(dLatitude));
		Log.d("TAG", String.valueOf(dLongitude));
		
		distance = Math.sqrt(Math.pow(dLatitude, 2) + Math.pow(dLongitude, 2));

		// w promieniu 2 km to zgodnosc do okolo 3-4 miejsca po przecinku we wspolrzednych
		if (distance > r){
			Toast.makeText(getApplicationContext(), "jestes poza obszarem", Toast.LENGTH_LONG).show();
			// send sms;
		}
	}

	public Location getLocation(){
		try{
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
			isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if( isGpsEnabled || isNetworkEnabled){
				this.canGetLocation = true;

				if(isNetworkEnabled){
					bestProvider = "network";
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
					if(locationManager != null){
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}
					if(location != null){
						longitude = location.getLongitude();
						latitude = location.getLatitude();
					}
				}
				
				if(isGpsEnabled){
					bestProvider = "gps";
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
					if(locationManager != null){
						location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					}
					if(location != null){
						longitude = location.getLongitude();
						latitude = location.getLatitude();
					}
				}
				
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return location;
	}
	
	public void updateTextViews(){

		//String str = "latitude " + getLatitude() + "\nlongitude" + getLongitude();
		//Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();

		t1.setText("najlepszy dostawca: " + bestProvider);
		t2.setText("longitude: " + getLongitude());
		t3.setText("latitude: " + getLatitude());

<<<<<<< HEAD
		t4.setText(t4.getText() + "" + getLongitude() + " / " + getLatitude() + "\n");

	}
	
	public void translateLongitude(){
		
		List<Address> addresses = null;
		try {
			Log.d("t", "j1");
			addresses = geocoder.getFromLocation(getLatitude(), getLongitude(), 1);
			Log.d("t", "j2");
			String result = "address: \n";

			if (addresses != null && addresses.size() > 0){
				Address address = addresses.get(0);

				for(int i = 0; i < address.getMaxAddressLineIndex(); i++){
					result += address.getAddressLine(i) + "\n";
				}
				result += "\n";

				t4.setText(t4.getText() + result + "\n");
			}
			else{
				t4.setText(t4.getText() + "can't find address \n");
			}
		} 
		catch (IOException e) {
			Toast.makeText(getApplicationContext(), 
					e.toString(), 
					Toast.LENGTH_LONG).show();
		}

	}
	
	public void stopUsingGps(){
		if(locationManager != null){
			locationManager.removeUpdates(GpsActivity.this);
		}
	}
	
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}
		return longitude;
	}
	
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}
		return latitude;
	}
	
	public boolean canGetLocation(){
		return this.canGetLocation;
	}
	
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
=======
	
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
>>>>>>> 36ae6a053960446e53f49e98ccadaeb204c5de5a
		
	}

	@Override
<<<<<<< HEAD
	public void onProviderEnabled(String arg0) {
=======
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
>>>>>>> 36ae6a053960446e53f49e98ccadaeb204c5de5a
		// TODO Auto-generated method stub
		
	}

	@Override
<<<<<<< HEAD
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
=======
	public void onProviderDisabled(String provider) {
>>>>>>> 36ae6a053960446e53f49e98ccadaeb204c5de5a
		// TODO Auto-generated method stub
		
	}

}
