package pl.soad.alzheimer_gps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GpsActivity extends Activity implements LocationListener{

	TextView t1, t2, t3, t4;
	EditText e1, e2, e3;
	Button b1;
	LocationManager locationManager;
	Location location;
	String bestProvider = "", reversedLocation = "", address;
	Geocoder geocoder, reverseGeocoder;
	AlarmManager alarmManager;
	Intent intent;
	PendingIntent pendingIntent;
	private double r; // km
	private double longitude, latitude, longitudeChecked, latitudeChecked, longitudeVar, latitudeVar,
	dLatitude, dLongitude, distance, longitudeReversed, latitudeReversed;
	Context context = GpsActivity.this;
	boolean canGetLocation = false;
	boolean isGpsEnabled = false;
	boolean isNetworkEnabled = false;
	private DatabaseManager dbManager;
	private SQLiteDatabase db;

	private static final long MIN_TIME = 1000 * 10 * 1; // 10 seconds
	private static final long MIN_DISTANCE = 0; // meters


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);

		t1 = (TextView) findViewById(R.id.gps_TextView1);
		t2 = (TextView) findViewById(R.id.gps_TextView2);
		t3 = (TextView) findViewById(R.id.gps_TextView3);
		t4 = (TextView) findViewById(R.id.gps_TextView4);
		b1 = (Button)   findViewById(R.id.gps_btn1);              



		geocoder = new Geocoder(this, Locale.getDefault());
		reverseGeocoder = new Geocoder(this, Locale.getDefault());

		getDataDB();

		t4.setText("Historia\n");

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startGettingLocation();
			}

		});
	}

	//pobranie promienia z bazy
	public void getDataDB(){
		dbManager = new DatabaseManager(this);
		db = dbManager.getReadableDatabase();

		String[] kolumny = {"address","range"};
		Cursor cursor = db.query("startpoint", kolumny, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			address = cursor.getString(0);
			r = Double.parseDouble(cursor.getInt(1)+"")/1000;
		}
		cursor.close();
		db.close();
		addressToLatLon(address);
	}

	public void addressToLatLon(String address){
		double[] temp = new double[2];
		temp = translateReverse(address);
		latitudeChecked = temp[0];
		longitudeChecked = temp[1];
	}


	@Override
	public void onLocationChanged(Location location) {
		getLocation();
		updateTextViews();
		checkLocation();
		translateLongitude();              
	}

	public void checkLocation(){   
		latitudeVar = latitudeChecked;	
		longitudeVar = longitudeChecked;

		longitude = longitude * 111.32;
		longitude = longitude * Math.cos(latitude);
		latitude *= 110.54;
		longitudeVar = longitudeVar * 111.32 * Math.cos(latitudeVar);
		latitudeVar *= 110.54;

		dLatitude = latitude - latitudeVar;
		dLongitude = longitude - longitudeVar;

		distance = Math.sqrt(Math.pow(dLatitude, 2) + Math.pow(dLongitude, 2));             
		Log.d("dist", String.valueOf(distance));
		if (distance > r){
			Toast.makeText(getApplicationContext(), "Jestes poza obszarem, odleg�o�� od �rodka: " + String.valueOf(distance*1000), Toast.LENGTH_LONG).show();
			// send sms;
		}
		else {
			Toast.makeText(getApplicationContext(), "Jestes w obszarze, odleg�o�� od �rodka: " + String.valueOf(distance*1000), Toast.LENGTH_LONG).show();
		}
	}

	public double[] translateReverse(String reversedLocation){

		try{
			List<Address> reverseAddresses = reverseGeocoder.getFromLocationName(reversedLocation, 1);
			if(reverseAddresses.size() > 0) {
				latitudeReversed = reverseAddresses.get(0).getLatitude();
				longitudeReversed = reverseAddresses.get(0).getLongitude();
			}
		}
		catch (IOException e) {
			Toast.makeText(getApplicationContext(),
					e.toString(),
					Toast.LENGTH_LONG).show();
		}

		double[] ret = new double[2];
		ret[0] = latitudeReversed;
		ret[1] = longitudeReversed;

		return ret;
	}

	public Location getLocation(){
		try{

			if(isNetworkEnabled){
				bestProvider = "network";
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
				if(locationManager != null){
					location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				}

				if(location != null){
					longitude = location.getLongitude();
					latitude = location.getLatitude();
				}
			}


		}
		catch(Exception e){
			e.printStackTrace();
		}

		return location;
	}

	public void startGettingLocation(){
		try{
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
			isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if( isGpsEnabled || isNetworkEnabled){
				this.canGetLocation = true;

				if(isNetworkEnabled){
					bestProvider = "network";
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);                                
				}

				if(isGpsEnabled){
					bestProvider = "gps";
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
				}
			}


		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void updateTextViews(){

		t1.setText("Najlepszy dostawca: " + bestProvider);
		t2.setText("Longitude: " + getLongitude());
		t3.setText("Latitude: " + getLatitude());

		t4.setText(t4.getText() + "" + getLongitude() + " / " + getLatitude() + "\n");

	}

	public void translateLongitude(){

		List<Address> addresses = null;
		try {
			//Log.d("t", "j1");
			addresses = geocoder.getFromLocation(getLatitude(), getLongitude(), 1);
			//Log.d("t", "j2");
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

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}