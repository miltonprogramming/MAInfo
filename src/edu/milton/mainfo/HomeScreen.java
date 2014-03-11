package edu.milton.mainfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class HomeScreen extends Activity {
	ImageButton flik;
	ImageButton saa;
	ImageButton settings;
	String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		flik = (ImageButton) findViewById(R.id.flik_button);
		saa = (ImageButton) findViewById(R.id.saa_button);
		settings = (ImageButton) findViewById(R.id.settings_button);
		Intent i = getIntent();
		if (i.getExtras() != null) {
			email = i.getStringExtra("email");
		}

		flik.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						FlikMenuActivity.class);
				i.putExtra("email", email);
				startActivity(i);

			}
		});

		saa.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						SAAScheduleActivity.class);
				startActivity(i);

			}
		});
		// TODO: this is a reference to settings class but we don't have a
		// settings activity?
		settings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Settings.class);
				startActivity(i);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}

}
