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
import android.support.design.widget.TabItem;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabItem;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
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

    Calendar myCalendar = Calendar.getInstance();
    String date;
    Date dateCalendar;
    String actualDay;
    Date actualDayCalendar;
    TextView textViewDate;
    Button buttonAfter;
    Dialog myDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_statistic, container, false);

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_statistic);
        //setContentView(R.layout.activity_statistic);

        this.textViewDate = (TextView) rootView.findViewById(R.id.dateView);
        setDate(textViewDate);

        ImageButton buttonCalendar = rootView.findViewById(R.id.calendar);
        rootView.findViewById(R.id.calendar).setBackgroundColor(Color.TRANSPARENT);
        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                showCalendar();
                changeDate(textViewDate);
            }
        });

        final ImageButton buttonBefore = rootView.findViewById(R.id.dayBefore);
        rootView.findViewById(R.id.dayBefore).setBackgroundColor(Color.TRANSPARENT);
        buttonBefore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                changeDateOneDayBefore(textViewDate);
            }
        });

        //this.buttonAfter = rootView.findViewById(R.id.dayAfter);
        this.buttonAfter =(Button)rootView.findViewById(R.id.dayAfter);
       // this.buttonAfter = (ImageButton) rootView.findViewById(R.id.dayAfter);
       // buttonAfter.setOnClickListener(this);
        //rootView.findViewById(R.id.dayAfter).setBackgroundColor(Color.TRANSPARENT);
        buttonAfter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                changeDateOneDayAfter(v);
            }
        });

        //myDialog = new Dialog(this);

        final ImageButton buttonTimeline = rootView.findViewById(R.id.timeline);
        rootView.findViewById(R.id.timeline).setBackgroundColor(Color.TRANSPARENT);
        buttonTimeline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                changeTimeline(v);
            }
        });

        PieChart pieChart = (PieChart) rootView.findViewById(R.id.piechart);
        setPieChart(pieChart);




        return rootView;
    }

    public void showCalendar() {

    }

    public void changeDate(TextView item) {
        item.setText(date);
    }

    public void changeDateOneDayBefore(View v) {
        TextView item = textViewDate;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateCalendar);
        calendar.roll(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");//formating according to my need
        date = formatter.format(calendar);
        dateCalendar = calendar.getTime();
        item.setText(date);
    }

    public void changeDateOneDayAfter(View v) {
        if(v.getId() == R.id.dayAfter) {
            TextView item = textViewDate;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateCalendar);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");//formating according to my need
            date = formatter.format(calendar);
            dateCalendar = calendar.getTime();
            item.setText(date);
        }
    }

    public void setDate (TextView item){
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");//formating according to my need
        date = formatter.format(today);
        dateCalendar = today;
        actualDay = formatter.format(today);
        actualDayCalendar = today;
        item.setText(date);
    }

    public void changeTimeline(View v) {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.activity_timeline_popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("M");
        btnFollow = (Button) myDialog.findViewById(R.id.ok);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void onRadioButtonClicked(View v) {

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

        PieDataSet dataSet = new PieDataSet(yvalues, "Getränkehaushalt");
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Ungesüsste Getränke");
        xVals.add("Sonstige Getränke");
        xVals.add("Koffeinhaltige Getränke");
        xVals.add("Alkoholhaltie Getränke");

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
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        pieChart.invalidate(); // refresh
    }



}
