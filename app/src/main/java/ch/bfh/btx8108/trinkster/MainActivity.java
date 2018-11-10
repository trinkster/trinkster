package ch.bfh.btx8108.trinkster;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int NR_OF_FRAGMENTS = 3;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private History fragment_history;
    private Statistic fragment_statistic;
    private GuessBAC fragment_guessbac;

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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return fragment_history;
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

    public void changeDateOneDayAfter(View v) {fragment_statistic.changeDateOneDayAfter(v); }

    public void changeDateOneDayBefore(View v) {fragment_statistic.changeDateOneDayBefore(v); }

    public void connectNearestClicked(View v){
        fragment_guessbac.connectNearestClicked(v);
    }

    public void disconnectClicked(View v){
        fragment_guessbac.disconnectClicked(v);
    }

    public void getFirmwareVersionClicked(View v){
        fragment_guessbac.getFirmwareVersionClicked(v);
    }

    public void getSerialNumberClicked(View v){
        fragment_guessbac.getSerialNumberClicked(v);
    }

    public void requestUseCountClicked(View v){
        fragment_guessbac.requestUseCountClicked(v);
    }

    public void requestBatteryVoltageClicked(View v){
        fragment_guessbac.requestBatteryVoltageClicked(v);
    }

    public void startBlowProcessClicked(View v) {
        fragment_guessbac.startBlowProcessClicked(v);
    }

}