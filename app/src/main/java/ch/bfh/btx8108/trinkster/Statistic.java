package ch.bfh.btx8108.trinkster;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.SimpleTimeZone;
import java.util.Locale;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.support.annotation.Nullable;
import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabItem;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ImageButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.view.View.OnClickListener;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;

public class Statistic extends Fragment {

    private static final String LOG_TAG = Statistic.class.getSimpleName();


    Calendar myCalendar = Calendar.getInstance();
    String date;
    Date dateCalendar;
    String actualDay;
    Date actualDayCalendar;
    TextView textViewDate;
    ImageButton buttonAfter;
    Dialog myDialog;
    String timeline;
    ImageButton buttonBefore;
    TextView errorMessage;
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy"); //formating according to my need
    View rootView;
    View secondView;
    View detailView;
    ImageButton backRootView;
    TextView drinkCategoryText;
    String drinkCategoryString = "Hallo";
    ListView list_drinks;
    ImageButton buttonTimeline;
    ImageButton buttonCalendar;
    PieChart pieChart;
    TextView textBack;
    int i;
    ArrayList<View> views = new ArrayList<>();
    LayoutInflater myInflater;
    ViewGroup myContainer;
    Bundle mySavedInstanceState;
    LinearLayout greyBarDetailsLayout;
    LinearLayout greyBarRootLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView() enter");

        this.myInflater = inflater;
        this.myContainer = container;
        this.mySavedInstanceState = savedInstanceState;

        //create rootView: activity_statistic
        this.rootView = inflater.inflate(R.layout.activity_statistic, container, false);
        views.add(rootView);

        //this.greyBarRootLayout = (LinearLayout) rootView.findViewById(R.id.greyBarRoot);

        this.textViewDate = (TextView) rootView.findViewById(R.id.dateView);
        setDate(textViewDate);

        this.buttonCalendar = rootView.findViewById(R.id.calendar);
        //rootView.findViewById(R.id.calendar).setBackgroundColor(Color.TRANSPARENT);
        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                showCalendar();
                changeDate(textViewDate);
            }
        });

        this.buttonBefore = rootView.findViewById(R.id.dayBefore);
        //rootView.findViewById(R.id.dayBefore).setBackgroundColor(Color.TRANSPARENT);

        this.buttonAfter =(ImageButton)rootView.findViewById(R.id.dayAfter);
        //rootView.findViewById(R.id.dayAfter).setBackgroundColor(Color.TRANSPARENT);

        //this.myDialog = new Dialog(this);

        this.buttonTimeline = (ImageButton) rootView.findViewById(R.id.timeline);
        //rootView.findViewById(R.id.timeline).setBackgroundColor(Color.TRANSPARENT);

        this.pieChart = (PieChart) rootView.findViewById(R.id.piechart);
        setPieChart(pieChart);

        this.errorMessage = (TextView) rootView.findViewById(R.id.error_message);

        //create detailView: activity_statistic_details
        this.detailView = inflater.inflate(R.layout.activity_statistic_details, container, false);
        views.add(detailView);

       // this greyBarDetailsLayout

        this.greyBarDetailsLayout = (LinearLayout) rootView.findViewById(R.id.greyBarDetails);

        this.backRootView = (ImageButton) rootView.findViewById(R.id.back);

        this.textBack = (TextView) rootView.findViewById(R.id.textBack);

        this.drinkCategoryText = (TextView) detailView.findViewById(R.id.drinkCategory);
        drinkCategoryText.setText(drinkCategoryString);

        this.list_drinks = (ListView) detailView.findViewById(R.id.listview_drinks);

        //buttonCalendar.setVisibility(View.INVISIBLE);

        //getActualView();
        //return rootView;

        //show rootView
        /*buttonCalendar.setVisibility(View.VISIBLE);
        buttonBefore.setVisibility(View.VISIBLE);
        buttonAfter.setVisibility(View.VISIBLE);
        buttonTimeline.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);*/

        greyBarDetailsLayout.setVisibility(View.INVISIBLE);
        backRootView.setVisibility(View.INVISIBLE);
        textBack.setVisibility(View.INVISIBLE);

        /*backRootView.setVisibility(View.INVISIBLE);
        textBack.setVisibility(View.INVISIBLE);
        drinkCategoryText.setVisibility(View.INVISIBLE);
        list_drinks.setVisibility(View.INVISIBLE);*/
        return rootView;
       // return detailView;
    }

    /*public void getActualView() {
        if
        rootView.setVisibility(View.INVISIBLE);
        detailView.setVisibility(View.VISIBLE);
        this.i = 1;
    }*/

    public void showCalendar() {

    }

    public void changeDate(TextView item) {
        item.setText(date);
    }

    public void changeDateOneDayBefore(View v) {
        Log.d(LOG_TAG, "changeDateOneDayBefore() enter");

//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(dateCalendar);
//        calendar.roll(Calendar.DAY_OF_MONTH, 1);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");//formating according to my need
//        date = formatter.format(calendar);
//        dateCalendar = calendar.getTime();
        this.textViewDate.setText("works");
    }

    public void changeDateOneDayAfter(View v) {
        Log.d(LOG_TAG, "changeDateOneDayAfter() enter");

//        myCalendar.setTime(dateCalendar);
//        myCalendar.add(Calendar.DAY_OF_MONTH, 1);
//        date = formatter.format(myCalendar);
//        dateCalendar = myCalendar.getTime();
        this.textViewDate.setText(":)");
    }

    public void setDate (TextView item){
        Date today = Calendar.getInstance().getTime();//getting date
        date = this.formatter.format(today);
        dateCalendar = today;
        actualDay = this.formatter.format(today);
        actualDayCalendar = today;
        item.setText(date);
    }

    public void changeTimeline(View v) {
//        TextView txtclose;
//        Button ok;
//        myDialog.setContentView(R.layout.activity_timeline_popup);
//        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
//        txtclose.setText("M");
//        ok = (Button) myDialog.findViewById(R.id.ok);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//            }
//        });
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.show();


    }

    public void closePopup(View v) {

        //Intent intent = new Intent(this, activity_statistic_details.class);
        //startActivity(intent);

    }

    public void onRadioButtonClicked(View v) {
        // Is the button checked?
        /*boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        // Set Sting timeline to the chosen timeline
        switch(v.getId()) {
            case R.id.radio_day:
                if (checked)
                    this.timeline = "day";
                    break;
            case R.id.radio_week:
                if (checked)
                    this.timeline = "week";
                    break;
            case R.id.radio_month:
                if (checked)
                    this.timeline = "month";
                    break;
            case R.id.radio_year:
                if (checked)
                    this.timeline = "year";
                    break;
        }*/
    }

    public void confirmPopup(View v) {
       /* boolean chosen = timeline.isEmpty();

        if (chosen!=true) {
            if (timeline.equals("day")) {
                changeToDay();
            } else if (timeline.equals("week")) {
                changeToWeek();
            } else if (timeline.equals("month")) {
                changeToMonth();
            } else if (timeline.equals("year")) {
                changeToYear();
            } else {
                this.errorMessage.setText("Fehler bei Speicherung der ausgewählten Zeitspanne. String wurde nicht abgespeichert.");
            }
        } else {
            this.errorMessage.setText("Es wurde keine Zeitspanne ausgewählt.");
        }*/
    }

    public void changeToDay() {
        textViewDate.setText("Es wird nun der Tag angezeigt.");
    }

    public void changeToWeek() {
        textViewDate.setText("Es wird nun die Woche angezeigt.");
    }

    public void changeToMonth() {
        textViewDate.setText("Es wird nun der Monat angezeigt.");
    }

    public void changeToYear() {
        textViewDate.setText("Es wird nun das Jahr angezeigt.");
    }

    public void setPieChart (PieChart pieChart) {
        pieChart.setUsePercentValues(true);

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(20f, 0));
        yvalues.add(new Entry(5f, 1));
        yvalues.add(new Entry(2f, 2));
        yvalues.add(new Entry(3f, 3));

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        //PieDataSet dataSet = new PieDataSet(yvalues, "Getränkehaushalt");
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Ungesüsste Getränke");
        xVals.add("Sonstige Getränke");
        xVals.add("Koffeinhaltige Getränke");
        xVals.add("Alkoholhaltige Getränke");

        PieData data = new PieData(xVals, dataSet);

        // In percentage Term
        data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));

        //set Data
        pieChart.setData(data);

        //set Colors
        int [] color={ Color.rgb(50,205,50), Color.rgb(255,160,122), Color.rgb(238,174,238),
                Color.rgb(150,205,205)
        };
        dataSet.setColors(color);

        //Disable Hole in the Pie Chart
        pieChart.setDrawHoleEnabled(false);

        //Text Size and Text Color
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);

        //legend
        Legend l = pieChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART_CENTER);
        //l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        pieChart.invalidate(); // refresh
    }

    public void showDetails(View v) {
       /*
        buttonBefore.setVisibility(View.INVISIBLE);
        buttonAfter.setVisibility(View.INVISIBLE);
        buttonTimeline.setVisibility(View.INVISIBLE);

        errorMessage.setVisibility(View.INVISIBLE);
        */
        //greyBarRootLayout.setVisibility(View.INVISIBLE);
        //textViewDate.setVisibility(View.INVISIBLE);
        //buttonCalendar.setVisibility(View.INVISIBLE);
        //buttonBefore.setVisibility(View.INVISIBLE);
        //buttonAfter.setVisibility(View.INVISIBLE);
        //buttonTimeline.setVisibility(View.INVISIBLE);

        //pieChart.setVisibility(View.INVISIBLE);
        //errorMessage.setVisibility(View.INVISIBLE);
        greyBarDetailsLayout.setVisibility(View.VISIBLE);
        backRootView.setVisibility(View.VISIBLE);
        textBack.setVisibility(View.VISIBLE);

        //return rootView;

    }

    public void goBack(View v) {
        greyBarDetailsLayout.setVisibility(View.GONE);
        backRootView.setVisibility(View.GONE);
        textBack.setVisibility(View.GONE);

       /* greyBarRootLayout.setVisibility(View.VISIBLE);
        buttonCalendar.setVisibility(View.VISIBLE);
        buttonBefore.setVisibility(View.VISIBLE);
        buttonAfter.setVisibility(View.VISIBLE);
        buttonTimeline.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);*/

       // backRootView.setVisibility(View.INVISIBLE);
       // textBack.setVisibility(View.INVISIBLE);
        /*
        drinkCategoryText.setVisibility(View.INVISIBLE);
        list_drinks.setVisibility(View.INVISIBLE);*/
       // this.i = 0;
       // onCreateView(myInflater, myContainer, mySavedInstanceState);
    }

}
