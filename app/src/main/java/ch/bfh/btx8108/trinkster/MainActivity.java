package ch.bfh.btx8108.trinkster;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int NR_OF_FRAGMENTS = 3;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private static History fragment_history;
    private static Statistic fragment_statistic;
    private static GuessBAC fragment_guessbac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragment_history = new History();
        fragment_statistic = new Statistic();
        fragment_guessbac = new GuessBAC();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflowmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            System.out.println("Settings");  // User chose the "Settings" item, show the app settings UI...
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if(mViewPager.getCurrentItem() == 0) {
            if (mSectionsPagerAdapter.getItem(0) instanceof HistoryAddFragment) {
                ((HistoryAddFragment) mSectionsPagerAdapter.getItem(0)).backPressed();
            }
            else if (mSectionsPagerAdapter.getItem(0) instanceof History) {
                finish();
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {
        private static final String LOG_TAG = SectionsPagerAdapter.class.getSimpleName();

        private final class HistoryListener implements HistoryFragmentListener {
            private final String LOG_TAG = HistoryListener.class.getSimpleName();

            public void onSwitchToNextFragment() {
                Log.d(LOG_TAG, "onSwitchToNextFragment: enter");
                mFragmentManager.beginTransaction().remove(mFragmentAtPos0).commitNow();

                if (mFragmentAtPos0 instanceof History){
                    mFragmentAtPos0 = HistoryAddFragment.createInstance(listener);
                }else{ // Instance of NextFragment
                    mFragmentAtPos0 = History.createInstance(listener);
                }
                notifyDataSetChanged();

                Log.d(LOG_TAG, "onSwitchToNextFragment: leave");
            }
        }

        private final FragmentManager mFragmentManager;
        public Fragment mFragmentAtPos0;
        HistoryListener listener = new HistoryListener();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if(mFragmentAtPos0 == null){
                        mFragmentAtPos0 = History.createInstance(listener);
                    }
                    return mFragmentAtPos0;
                case 1:
                    return fragment_statistic;
                case 2:
                    return fragment_guessbac;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() { return NR_OF_FRAGMENTS; }

        @Override
        public int getItemPosition(Object object)
        {
            if (object instanceof History && mFragmentAtPos0 instanceof HistoryAddFragment) {
                return POSITION_NONE;
            }
            if (object instanceof HistoryAddFragment && mFragmentAtPos0 instanceof History) {
                return POSITION_NONE;
            }
            return POSITION_UNCHANGED;
        }
    }

    public History getFragment_history() {
        return fragment_history;
    }

    public Statistic getFragment_statistic() {
        return fragment_statistic;
    }

    public GuessBAC getFragment_guessbac() {
        return fragment_guessbac;
    }

    /*
    Nachfolgend sind Weiterleitungen fuer Events (aktuell: onClick-Events) aufgefuehrt.

    Vorgehen:
    - Im Layout beim Button-Tag ein android:onClick="myMethodName"-Attribut definieren
    - In dieser Klasse (MainActivity) eine Methode mit dem selben Methodennamen erstellen
      - Wichtig: Als Argument immer eine View übergeben!
    - In der entsprechenden Methode jeweils von der Fragment-Instanz die notwendige Methode aufrufen.

    Nachfolgend ein paar Beispiele:
     */

    // History
    public void addDrink(View v){

        fragment_history.addDrink(v);
    }

    //Statistic
    public void showCalendar(View v) {
        fragment_statistic.showCalendar(v);
    }

    public void confirmCalendar(View v) {
        fragment_statistic.confirmCalendar(v);
    }

    public void changeDateOneDayAfter(View v) {
        Log.d(LOG_TAG, "changeDateOneDayAfter() enter");

        fragment_statistic.changeDateOneDayAfter(v);
    }

    public void thisDay(View v) {
        fragment_statistic.thisDay(v);
    }

    public void changeDateOneDayBefore(View v) {
        fragment_statistic.changeDateOneDayBefore(v);
    }

    public void changeTimeline(View v) {
        fragment_statistic.changeTimeline(v);
    }

    public void goBack(View v) {
        fragment_statistic.goBack(v);
    }

    //Guess BAC
    public void startBreathalyzerComparison(View v){
        fragment_guessbac.startBreathalyzerComparison(v);
    }

}