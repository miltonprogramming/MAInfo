package edu.milton.mainfo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Welcome extends FragmentActivity implements ExistingAccountDialogFragment.NoticeDialogListener{
	private Button submit;
	private Button login;
	private EditText email;
	private ProgressDialog pDialog;
	JSONParser jsonParser;
	public Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		setTitle("Welcome!");
		jsonParser = new JSONParser();
		SharedPreferences sp = getSharedPreferences("login",0);
		edit = sp.edit();
		email = (EditText) findViewById(R.id.welcome_email);
		email.setText(sp.getString("username", null));
		submit = (Button) findViewById(R.id.welcome_button);
		login = (Button) findViewById(R.id.welcome_login_button);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Register().execute();
				
			}
		});
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Welcome.this, Login.class);
				startActivity(i);
				finish();
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
			String emailString = email.getText().toString();
			if(emailString.equals(null)){
				emailString = "No Email Entered";
			}
			JSONObject json = jParser.getJSONFromUrl("http://www.ma1geek.org/app_users/register.php?n="+ emailString);

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
					Log.d("previous account detected!", json.getString("message"));
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Welcome.this);
					Editor edit = sp.edit();
					edit.putString("username", emailString);
					edit.commit();
					showNoticeDialog();
					
					return (null);
				}

				if (action == 2) {
					Log.d("Registered!", json.getString("message"));
					edit.putString("username", emailString);
					edit.commit();
					Intent i = new Intent(getApplicationContext(), Login.class);
					//finish();
					startActivity(i);
					return (message);
				}
				if (action == 1) {
					Log.d("bad email address!", json.getString("message"));
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
	
	class Reset extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			/*pDialog = new ProgressDialog(Welcome.this);
			pDialog.setMessage("Working...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();*/
		}

		protected String doInBackground(String... args) 
		{

			final JSONParser jParser = new JSONParser();
			final String emailString = email.getText().toString();
			jParser.makeSimpleGetRequest("http://www.ma1geek.org/app_users/reset.php?n="+ emailString);
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
	public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ExistingAccountDialogFragment();
        dialog.show(getSupportFragmentManager(), "ExistingAccount");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
    	new Reset().execute();
    	//Intent i = new Intent(getApplicationContext(), Login.class);
		finish();
		//startActivity(i);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    	//Intent i = new Intent(getApplicationContext(), Login.class);
		finish();
		//startActivity(i);
       
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}
	
	

}


