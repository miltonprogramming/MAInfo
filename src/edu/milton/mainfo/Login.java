package edu.milton.mainfo;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private EditText user, pass;
	private Button submit;
	private ProgressDialog pDialog;
	public static final String LOGIN_URL = "http://www.ma1geek.org/app_users/Login.php";
	JSONParser jsonParser;
	SharedPreferences sp;
	Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		jsonParser = new JSONParser();
		sp = PreferenceManager.getDefaultSharedPreferences(Login.this);
		edit = sp.edit();
		user = (EditText)findViewById(R.id.editText1);
		pass = (EditText)findViewById(R.id.editText2);
		submit = (Button)findViewById(R.id.button1);
		user.setText(sp.getString("username", "error"));
		pass.setText(sp.getString("password", "error"));
		submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	new AttemptLogin().execute();
                
            }
        });
	}
	
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
				new AttemptLogin().execute();
			break;
			
		case R.id.button2:
			Intent i = new Intent(getApplicationContext(), Welcome.class);
			startActivity(i);
		break;	
		

		default:
			break;
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
           pDialog = new ProgressDialog(Login.this);
           pDialog.setMessage("Attempting login...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }
		
		@Override
		protected String doInBackground(String... args) {
           String username = user.getText().toString();
           String password = pass.getText().toString();
           JSONObject json = jsonParser.getJSONFromUrl("http://www.ma1geek.org/app_users/Login.php?n="+username+"&p="+password);
           //List<NameValuePair> params = new ArrayList<NameValuePair>();
           //params.add(new BasicNameValuePair("username", username));
          // params.add(new BasicNameValuePair("password", password));
           //JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
          
           try 
           {
               int success = json.getInt("success");
               if (success == 1) 
               {
               	Log.d("Login Successful!", json.toString());
					edit.putString("username", username);
					edit.putString("password", password);
					edit.commit();
					Intent i = new Intent(Login.this, HomeScreen.class);
					finish();
					startActivity(i);
					return json.getString("message");
               	}
                if(success == 2)
                {
                	Log.d("Login","login failed: success = 2");
					edit.putString("username", username);
					edit.remove("password");
					edit.commit();
					return json.getString("message");
                }
                else{
                	edit.remove("username");
                	edit.remove("password");
					edit.commit();
					return json.getString("message");
                	
                }
           } 
           catch (JSONException e) {
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
           if (file_url != null){
           	Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
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
