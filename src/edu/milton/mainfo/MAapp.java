package edu.milton.mainfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class MAapp extends Activity {
	public JSONParser jsonParser;
	public Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maapp);
		jsonParser = new JSONParser();
		SharedPreferences sp = getSharedPreferences("Login", 0);
		edit = sp.edit();
		if(sp.getBoolean("has_run", false)==true)
		{
			Intent i = new Intent(getApplicationContext(), Login.class);
			//startActivity(i);
			finish();
			startActivity(i);
		}
		else{
		edit.putBoolean("has_run", true);
		edit.commit();
		Intent i = new Intent(getApplicationContext(), Welcome.class);
		startActivity(i);
		finish();
		}
		
	}

}