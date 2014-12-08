package pl.soad.alzheimer_gps;

import pl.soad.alzheimer_gps.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button btn_turnonoff;
	private Button btn_contacts;
	private Button btn_settings;
	private DatabaseManager dbmanager;
	
	private boolean onoff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dbmanager = new DatabaseManager(this);
		
		btn_turnonoff = (Button) findViewById(R.id.btn_turnonoff);
		btn_contacts = (Button) findViewById(R.id.btn_contacts);
		btn_settings = (Button) findViewById(R.id.btn_settings);
		
		onoff = false;
		
		btn_turnonoff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (onoff) {
					btn_turnonoff.setText("W³¹cz œledzenie");
					onoff = false;
				}
				else {
					btn_turnonoff.setText("Wy³¹cz œledzenie");
					onoff = true;
				}
				
			}
		});
		
		btn_settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbmanager.sendSms();
				
			}
		});
		
		btn_contacts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), DisplayActivity.class);
		        startActivity(i);	
			}
		});
		    
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
