package edu.milton.mainfo;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SAAListFrag extends ListFragment implements LoaderCallbacks<Cursor> {

	private ProgressDialog pDialog;
	private static final String READ_EVENTS_URL = "http://saa.ma1geek.org/sqlretrieve.php";
	private JSONArray retrievedEvents = null;
	private ArrayList<SAAEvent> eventList;
	
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    new LoadEvents().execute();
    Log.d("SAAListFrag","fragment created");
    
    
    
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
          Bundle savedInstanceState) {
  View view = inflater.inflate(R.layout.list_frag_layout, null);
  return view;
  }


  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    // do something with the data

  }
  public void updateJSONdata() {
	
	  try {
		  eventList = new ArrayList<SAAEvent>();
      JSONParser jParser = new JSONParser();   
      JSONObject json = jParser.getJSONFromUrl(READ_EVENTS_URL);

      
          retrievedEvents = json.getJSONArray("Activities");
          for (int i = 0; i < retrievedEvents.length(); i++) {
              JSONObject c = retrievedEvents.getJSONObject(i);
              eventList.add(new SAAEvent(c));
          }
      } catch (JSONException e) {
          e.printStackTrace();
      }
  }
  

@Override
public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
	// TODO Auto-generated method stub
	
}

@Override
public void onLoaderReset(Loader<Cursor> arg0) {
	// TODO Auto-generated method stub
	
}
public void updateEventList(){
	SAAArrayAdapter adapter = new SAAArrayAdapter(getActivity(), eventList);
    setListAdapter(adapter);
}
public class LoadEvents extends AsyncTask<Void, Void, Boolean> {

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading Events...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}
    @Override
    protected Boolean doInBackground(Void... arg0) {
    	//we will develop this method in version 2
    	Log.d("LoadEvents", "attempting to load events");
    	try {
    		updateJSONdata();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        
        return null;

    }


    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        pDialog.dismiss();
        updateEventList();
    }
}
}


class SAAArrayAdapter extends ArrayAdapter<Object> {
    private final Context context;      
    private ArrayList<SAAEvent> Values;
    int which;
    int currentlyExpandedItem = -1;
    SimpleDateFormat dateOutputter;
    SimpleDateFormat timeOutputter;
    

    @SuppressWarnings("unchecked")
	public SAAArrayAdapter(Context context, ArrayList<? extends Object> Values) {
        super(context, R.layout.flik_food_view, (ArrayList<Object>)Values);
        this.context = context;
        this.Values=(ArrayList<SAAEvent>)Values;
        dateOutputter = new SimpleDateFormat("EEE, MMMMM d");
        timeOutputter = new SimpleDateFormat("h:mm a");
    }
    
    
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
    	final int position = pos;
    	View rowView;
    
    	SAAEvent rowItem = Values.get(position);
    	if (position == currentlyExpandedItem) {
    		LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		rowView = inflater.inflate(R.layout.event_expanded_view, parent, false);
        		TextView eventTitleTextView = (TextView) rowView.findViewById(R.id.event_title_textview);
        		TextView dateLocationTextView = (TextView) rowView.findViewById(R.id.date_location_textview);
        		TextView signUpTextView = (TextView) rowView.findViewById(R.id.signup_textview);
        		if(rowItem.isSignUp()) {
        			signUpTextView.setText("Sign Up Required");
        		} else {
        			signUpTextView.setText("");
        		}
        		TextView eventDescriptionTextView = (TextView) rowView.findViewById(R.id.event_description_textview);
        		eventDescriptionTextView.setText(rowItem.getEventDescription());
        		String eventStartTime = timeOutputter.format(rowItem.getEventBeginTime(), new StringBuffer(), new FieldPosition(0)).toString();
        		dateLocationTextView.setText(eventStartTime + " location placeholder");
        		@SuppressWarnings("unused")
				ImageView eventIconImageView = (ImageView) rowView.findViewById(R.id.event_icon); //this is temporary until we have support for icons for individual events
        		eventTitleTextView.setText(rowItem.getEventName());
        		
        		//textView.setGravity(Gravity.CENTER_HORIZONTAL);
        		eventTitleTextView.setTextIsSelectable(false);
        		eventTitleTextView.getPaint().setAntiAlias(true);
        		ImageButton collapseButton = (ImageButton) rowView.findViewById(R.id.collapse_button);
        		collapseButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        currentlyExpandedItem = -1;
                        notifyDataSetChanged();
                    }
                });
    	}
    	else {
    		LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		rowView = inflater.inflate(R.layout.event_compact_view, parent, false);
    		TextView eventTitleTextView = (TextView) rowView.findViewById(R.id.event_title_textview);
    		TextView dateLocationTextView = (TextView) rowView.findViewById(R.id.date_location_textview);
    		String eventStartTime = timeOutputter.format(rowItem.getEventBeginTime(), new StringBuffer(), new FieldPosition(0)).toString();
    		dateLocationTextView.setText(eventStartTime + " - " + rowItem.getEventLocation());
    		TextView signUpTextView = (TextView) rowView.findViewById(R.id.signup_textview);
    		if(rowItem.isSignUp()) {
    			signUpTextView.setText("Sign Up Required");
    		} else {
    			signUpTextView.setText("");
    		}
    		@SuppressWarnings("unused")
			ImageView eventIconImageView = (ImageView) rowView.findViewById(R.id.event_icon); //this is temporary until we have support for icons for individual events
    		eventTitleTextView.setText(rowItem.getEventName());
    		
    		//textView.setGravity(Gravity.CENTER_HORIZONTAL);
    		eventTitleTextView.setTextIsSelectable(false);
    		eventTitleTextView.getPaint().setAntiAlias(true);
    		ImageButton expandButton = (ImageButton) rowView.findViewById(R.id.expand_button);
    		expandButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    currentlyExpandedItem = position;
                    notifyDataSetChanged();
                }
            });
            
    	}
        return rowView;
    }
}