package edu.milton.mainfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.milton.mainfo.SAAListFrag.LoadEvents;

import android.app.ProgressDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FlikListFrag extends ListFragment implements LoaderCallbacks<Cursor> {
	
	private ProgressDialog pDialog;
	private static final String READ_EVENTS_URL = "http://flik.ma1geek.org/getMeals.php";
	private JSONArray retrievedEntrees = null;
	private JSONArray retrievedDesserts = null;
	private JSONArray retrievedFlikLive = null;
	private JSONArray retrievedSides = null;
	private JSONArray retrievedSoups = null;
	private ArrayList<MenuItem> Foods;	
	private String date;
	private String type = "Entree";
	
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	  	Calendar c = Calendar.getInstance();
	  	System.out.println("Current time => " + c.getTime());
	  	SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
	  	String formattedDate = df.format(c.getTime());
	  	date = formattedDate;
	    //date = "2013-11-23";
	    //use to demonstrate if there are no items for current date
	    
	    Context context = getActivity();  
	    new LoadMeals().execute();
	    Log.d("FlikListFrag","fragment created");	  
  }
  public void updateJSONdata() {
	  
	  //get Entrees
	  try {
		  Foods = new ArrayList<MenuItem>();
		  JSONParser jParser = new JSONParser();   
		  JSONObject json = jParser.getJSONFromUrl(READ_EVENTS_URL + "?type=Entree&date="+date);

      
          retrievedEntrees = json.getJSONArray("Meals");
          Foods.add(new MenuItem(true, "Entrees"));
          for (int i = 0; i < retrievedEntrees.length(); i++) {
              JSONObject c = retrievedEntrees.getJSONObject(i);
              System.out.println(c);
              Foods.add(new MenuItem(false, c));
          }
      } catch (JSONException e) {
          e.printStackTrace();
      }
	  //get Sides
	  try {
		  JSONParser jParser = new JSONParser();   
		  JSONObject json = jParser.getJSONFromUrl(READ_EVENTS_URL + "?type=Side&date="+date);

      
          retrievedSides = json.getJSONArray("Meals");
          Foods.add(new MenuItem(true, "Sides"));
          for (int i = 0; i < retrievedSides.length(); i++) {
              JSONObject c = retrievedSides.getJSONObject(i);
              System.out.println(c);
              Foods.add(new MenuItem(false, c));
          }
      	}catch (JSONException e) {
          e.printStackTrace();
      }	  
	  //get Flik Live
	  try {
		  JSONParser jParser = new JSONParser();   
		  JSONObject json = jParser.getJSONFromUrl(READ_EVENTS_URL + "?type=Flik+Live&date="+date);

      
          retrievedFlikLive = json.getJSONArray("Meals");
          Foods.add(new MenuItem(true, "Flik Live"));
          for (int i = 0; i < retrievedFlikLive.length(); i++) {
              JSONObject c = retrievedFlikLive.getJSONObject(i);
              System.out.println(c);
              Foods.add(new MenuItem(false, c));
          }
      	}catch (JSONException e) {
          e.printStackTrace();
      }
	  //get Dessert
	  try {
		  JSONParser jParser = new JSONParser();   
		  JSONObject json = jParser.getJSONFromUrl(READ_EVENTS_URL + "?type=Dessert&date="+date);

      
          retrievedDesserts = json.getJSONArray("Meals");
          Foods.add(new MenuItem(true, "Desserts"));
          for (int i = 0; i < retrievedDesserts.length(); i++) {
              JSONObject c = retrievedDesserts.getJSONObject(i);
              System.out.println(c);
              Foods.add(new MenuItem(false, c));
          }
      	}catch (JSONException e) {
          e.printStackTrace();
      }	  
	  //get Soup
	  try {
		  JSONParser jParser = new JSONParser();   
		  JSONObject json = jParser.getJSONFromUrl(READ_EVENTS_URL + "?type=Soup&date="+date);

      
          retrievedSoups = json.getJSONArray("Meals");
          Foods.add(new MenuItem(true, "Soups"));
          for (int i = 0; i < retrievedSoups.length(); i++) {
              JSONObject c = retrievedSoups.getJSONObject(i);
              System.out.println(c);
              Foods.add(new MenuItem(false, c));
          }
      	}catch (JSONException e) {
          e.printStackTrace();
      }	  		  
  }
  
  public class LoadMeals extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading Meals...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
	    @Override
	    protected Boolean doInBackground(Void... arg0) {
	    	//we will develop this method in version 2
	    	Log.d("LoadEvents", "attempting to load meals");
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
  
  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    // do something with the data

  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
          Bundle savedInstanceState) {
  View view = inflater.inflate(R.layout.list_frag_layout, null);
  return view;
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
	FlikArrayAdapter adapter = new FlikArrayAdapter(getActivity(), Foods);
    setListAdapter(adapter);
}
}
class FlikArrayAdapter extends ArrayAdapter<Object> {
    private final Context context;      
    private ArrayList<? extends Object> Values;
    int which;
    int currentlyExpandedItem = -1;

    @SuppressWarnings("unchecked")
	public FlikArrayAdapter(Context context, ArrayList<? extends Object> Values) {
        super(context, R.layout.flik_food_view, (ArrayList<Object>)Values);
        this.context = context;
        this.Values=Values;
        
    }
    
    
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
    	final int position = pos;
    	View rowView;
    
    	MenuItem rowItem = (MenuItem)Values.get(position);
    	if (position == currentlyExpandedItem) {
    		LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		rowView = inflater.inflate(R.layout.flik_food_alt_view, parent, false);
    	}
    	else if (!rowItem.isHeading()) {
    		LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		rowView = inflater.inflate(R.layout.flik_food_view, parent, false);
    		TextView textView = (TextView) rowView.findViewById(R.id.food_text_view);
    		
    		ImageButton good = (ImageButton) rowView.findViewById(R.id.good_button);
    		ImageButton bad = (ImageButton) rowView.findViewById(R.id.bad_button);
    		textView.setText(rowItem.getItemName());
    		textView.setTextSize(12);
    		//textView.setGravity(Gravity.CENTER_HORIZONTAL);
    		textView.setTextIsSelectable(false);
    		textView.getPaint().setAntiAlias(true);
    		rowView.getLayoutParams().height=36;
    		
            good.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "good "+position, Toast.LENGTH_SHORT).show();
                }
            });
            bad.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "bad "+position, Toast.LENGTH_SHORT).show();
                }
            });
    	} else {
    		LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	rowView = inflater.inflate(R.layout.flik_food_view, parent, false);
       		TextView textView = (TextView) rowView.findViewById(R.id.food_text_view);
       		textView.setText(rowItem.getItemName());
       		textView.setTextSize(18);
       		textView.getPaint().setAntiAlias(true);
       		
    		ImageButton good = (ImageButton) rowView.findViewById(R.id.good_button);
    		ImageButton bad = (ImageButton) rowView.findViewById(R.id.bad_button);
    		textView.setTextIsSelectable(false);
    		//textView.setPadding(DPConverter.dpConvert(2),0,0,0);
    		textView.setTypeface(Typeface.DEFAULT_BOLD);
    		
    		good.setVisibility(View.GONE);
    		bad.setVisibility(View.GONE);
    	}
    	


        // Change icon based on name
     
        

        return rowView;
    }
}