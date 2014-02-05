package edu.milton.mainfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SAAScheduleActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs_test);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// super.onCreateOptionsMenu(menu);
		//Log.d("Action Bar", "action bar being deployed");
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Settings Button Pressed", Toast.LENGTH_SHORT)
					.show();
			return true;
		case R.id.action_back:
			Toast.makeText(this, "Back Button Pressed", Toast.LENGTH_SHORT)
					.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Log.d("SAAScheduleActivity", "attempting to get saalistfragitem");
			return new SAAListFrag(position);
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			Calendar c = Calendar.getInstance(l);
			SimpleDateFormat dfCur = new SimpleDateFormat("EEE");
			String curDate = dfCur.format(c.getTime());

			// adjust date to show friday of this weekend
			if (curDate.equals("Mon")) {
				c.add(Calendar.DATE, 3);
			} else if (curDate.equals("Tue")) {
				c.add(Calendar.DATE, 2);
			} else if (curDate.equals("Wed")) {
				c.add(Calendar.DATE, 1);
			} else if (curDate.equals("Thu")) {
				c.add(Calendar.DATE, 0);
			} else if (curDate.equals("Fri")) {
				c.add(Calendar.DATE, -1);
			} else if (curDate.equals("Sat")) {
				c.add(Calendar.DATE, -2);
			} else if (curDate.equals("Sun")) {
				c.add(Calendar.DATE, -3);
			}

			// format date title on section heading
			SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d");
			String formattedDate[] = new String[getCount()];
			// leftmost title
			formattedDate[0] = df.format(c.getTime());
			c.add(Calendar.DATE, 1);
			// 2nd title
			formattedDate[1] = df.format(c.getTime());
			c.add(Calendar.DATE, 1);
			// 3rd title
			formattedDate[1] = df.format(c.getTime());
			c.add(Calendar.DATE, 1);
			// 4th title
			formattedDate[2] = df.format(c.getTime());

			switch (position) {
			case 0:
				return formattedDate[0];
			case 1:
				return formattedDate[1];
			case 2:
				return formattedDate[2];
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		public static final String ARG_SECTION_TEXT = "This is Section N";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tabs_test_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			TextView secTextView = (TextView) rootView
					.findViewById(R.id.seclabel2);
			secTextView.setText(ARG_SECTION_TEXT);
			return rootView;
		}
	}

}
