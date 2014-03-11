package edu.milton.mainfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends Activity {
	private Button logout;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		SharedPreferences sp = getSharedPreferences("login",0);
		final Editor edit = sp.edit();
		tv = (TextView) findViewById(R.id.ApacheTextView);
		tv.setText(Html.fromHtml("Copyright 2014 Milton Academy Programming Applications <br> Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at <br><br> <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">http://www.apache.org/licenses/LICENSE-2.0</a> <br><br> Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License."));
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
