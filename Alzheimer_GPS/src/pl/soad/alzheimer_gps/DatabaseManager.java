package pl.soad.alzheimer_gps;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SmsManager;


public class DatabaseManager extends SQLiteOpenHelper {

	public DatabaseManager(Context context){
		super(context,"kontakty.db",null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table telefony(" +
				"nr integer primary key autoincrement," +
				"imie text," +
				"nazwisko text," +
				"telefon text);" +
				"");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS telefony");
		onCreate(db);	
	}
	
	
	public void sendSms () {
		
		SmsManager sms = null;
		String wiadomosc = "Ola";
		sms = SmsManager.getDefault();
		
		SQLiteDatabase db = getReadableDatabase();
		String kolumny[] = {"telefon"+""};
		Cursor cursor = db.query("telefony", kolumny, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do {
				sms.sendTextMessage((cursor.getString(0)), null, wiadomosc, null, null);
			} while(cursor.moveToNext());
		}

	}
}