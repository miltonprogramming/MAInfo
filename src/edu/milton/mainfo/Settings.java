package edu.milton.mainfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Settings extends Activity {
	private Button logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		SharedPreferences sp = getSharedPreferences("login",0);
		final Editor edit = sp.edit();
		logout = (Button) findViewById(R.id.logout_button);
		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				edit.remove("username");
				edit.remove("password");
				edit.commit();
				Intent i = new Intent(Settings.this, Login.class);
				startActivity(i);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
