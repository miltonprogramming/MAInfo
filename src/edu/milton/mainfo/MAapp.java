package edu.milton.mainfo;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MAapp extends Activity {
	private ProgressDialog pDialog;
	private String stu, stp;
	public JSONParser jsonParser;
	public Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maapp);
		jsonParser = new JSONParser();
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(MAapp.this);
		stu = sp.getString("username", null);
		stp = sp.getString("password", null);
		edit = sp.edit();
		if (stu == "error") {
			Intent i = new Intent(getApplicationContext(), Welcome.class);
			finish();
			startActivity(i);
		}
		if (stu != null && stp != null) {
			new AttemptLogin().execute();
		} else {
			Intent i = new Intent(getApplicationContext(), Login.class);
			finish();
			startActivity(i);
		}
	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MAapp.this);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			JSONObject json = jsonParser
					.getJSONFromUrl("http://www.ma1geek.org/app_users/Login.php?n="
							+ stu + "&p=" + stp);

			try {
				int success = json.getInt("success");
				if (success == 1) {
					Log.d("Login Successful!", json.toString());
					Intent i = new Intent(MAapp.this, HomeScreen.class);
					finish();
					startActivity(i);
					return json.getString("message");
				}
				if (success == 2) {
					edit.remove("password");
					edit.commit();
					Intent i = new Intent(MAapp.this, Login.class);
					finish();
					startActivity(i);
					return json.getString("message");
				} else {
					edit.remove("username");
					edit.remove("password");
					edit.commit();
					Intent i = new Intent(MAapp.this, Welcome.class);
					finish();
					startActivity(i);
					return json.getString("message");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(MAapp.this, file_url, Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}
}