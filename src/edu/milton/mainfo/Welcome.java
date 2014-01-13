package edu.milton.mainfo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Welcome extends Activity {
	private Button submit;
	private EditText email;
	private ProgressDialog pDialog;
	JSONParser jsonParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		setTitle("Welcome!");
		jsonParser = new JSONParser();
		email = (EditText) findViewById(R.id.welcome_email);
		submit = (Button) findViewById(R.id.welcome_button);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Register().execute();
			}
		});

	}

	class Register extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Welcome.this);
			pDialog.setMessage("Working...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			final JSONParser jParser = new JSONParser();
			final String emailString = email.getText().toString();
			JSONObject json = jParser
					.getJSONFromUrl("http://www.ma1geek.org/app_users/register.php?n="
							+ emailString);

			// when parsing JSON stuff, we should probably
			// try to catch any exceptions:
			try {

				// I know I said we would check if "Posts were Avail."
				// (success==1)
				// before we tried to read the individual posts, but I lied...
				// mComments will tell us how many "posts" or comments are
				// available

				int action = json.getInt("result");
				String message = json.getString("message");

				if (action == 3) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Welcome.this);
					builder.setTitle("Previous registration detected");
					builder.setPositiveButton("Reset Password",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User clicked OK button
									jParser.makeSimpleGetRequest("http://www.ma1geek.org/app_users/Reset?n="
											+ emailString);
									SharedPreferences sp = PreferenceManager
											.getDefaultSharedPreferences(Welcome.this);
									Editor edit = sp.edit();
									edit.putString("username", emailString);
									edit.commit();
									Intent i = new Intent(
											getApplicationContext(),
											Login.class);
									finish();
									startActivity(i);
								}
							});
					builder.setNegativeButton("Go to Login",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									SharedPreferences sp = PreferenceManager
											.getDefaultSharedPreferences(Welcome.this);
									Editor edit = sp.edit();
									edit.putString("username", emailString);
									edit.commit();
									Intent i = new Intent(
											getApplicationContext(),
											Login.class);
									finish();
									startActivity(i);
								}
							});
					return ("Check your email for further instructions.");
				}

				if (action == 2) {
					Intent i = new Intent(getApplicationContext(), Login.class);
					finish();
					startActivity(i);
					return (message);
				}
				if (action == 1) {
					return (message);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(Welcome.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}// register

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}

}
