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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

public class Statistic extends Fragment implements OnChartValueSelectedListener {
    private static final String LOG_TAG = Statistic.class.getSimpleName();

    String date;
    Date dateCalendar;
    String actualDay;
    Date actualDayCalendar;
    String weekDate;
    Date weekDateCalendar;
    String monthDate;
    Date monthDateCalendar;
    String yearDate;
    Date yearDateCalendar;

    TextView textViewDate;
    ImageButton buttonAfter;
    Dialog myDialog;
    String timeline = "day";
    ImageButton buttonBefore;
    TextView errorMessage;
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy"); //formating according to my need
    View rootView;
    View secondView;
    View detailView;
    ImageButton backRootView;
    TextView drinkCategoryText;
    String drinkCategoryString;
    ListView list_drinks;
    ImageButton buttonTimeline;
    ImageButton buttonCalendar;
    PieChart pieChart;
    TextView textBack;
    LinearLayout greyBarDetailsLayout;
    LinearLayout greyBarRootLayout;
    LinearLayout detailsLayoutLinear;
    LinearLayout detailsTotalLayout;
    ImageView imageDetails;
    TextView textViewTotal;
    PieDataSet dataSet;
    ArrayList<String> xVals;
    LinearLayout pieChartLinearLayout;
    LinearLayout layoutPopup;
    TextView textViewClose;
    LinearLayout textLinearLayout;
    TextView textViewChange;
    LinearLayout radioButtonLinearLayout;
    RadioGroup radioGroup;
    RadioButton radioDay;
    RadioButton radioWeek;
    RadioButton radioMonth;
    RadioButton radioYear;
    Button btnOk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView() enter");

        //create rootView: activity_statistic
        this.rootView = inflater.inflate(R.layout.activity_statistic, container, false);

        //this.greyBarRootLayout = (LinearLayout) rootView.findViewById(R.id.greyBarRoot);

        this.textViewDate = (TextView) rootView.findViewById(R.id.dateView);
        setDate(textViewDate);
        this.buttonCalendar = rootView.findViewById(R.id.calendar);
        //rootView.findViewById(R.id.calendar).setBackgroundColor(Color.TRANSPARENT);
        this.buttonBefore = rootView.findViewById(R.id.dayBefore);
        //rootView.findViewById(R.id.dayBefore).setBackgroundColor(Color.TRANSPARENT);
        this.buttonAfter =(ImageButton)rootView.findViewById(R.id.dayAfter);
        //rootView.findViewById(R.id.dayAfter).setBackgroundColor(Color.TRANSPARENT);
        this.buttonTimeline = (ImageButton) rootView.findViewById(R.id.timeline);
        //rootView.findViewById(R.id.timeline).setBackgroundColor(Color.TRANSPARENT);
        this.pieChartLinearLayout = (LinearLayout) rootView.findViewById(R.id.pieChartLayout);
        this.pieChart = (PieChart) rootView.findViewById(R.id.piechart);
        setPieChart(pieChart);

        this.errorMessage = (TextView) rootView.findViewById(R.id.error_message);

        //create detailView: activity_statistic_details
        this.greyBarDetailsLayout = (LinearLayout) rootView.findViewById(R.id.greyBarDetails);
        this.backRootView = (ImageButton) rootView.findViewById(R.id.back);
        this.textBack = (TextView) rootView.findViewById(R.id.textBack);
        this.detailsLayoutLinear = (LinearLayout) rootView.findViewById(R.id.detailsLayout);
        this.drinkCategoryText = (TextView) rootView.findViewById(R.id.drinkCategory);
        drinkCategoryText.setText(drinkCategoryString);
        this.list_drinks = (ListView) rootView.findViewById(R.id.listview_drinks);
        this.detailsTotalLayout = (LinearLayout) rootView.findViewById(R.id.detailsTotal);
        this.imageDetails = (ImageView) rootView.findViewById(R.id.image);
        this.textViewTotal = (TextView) rootView.findViewById(R.id.total);

        //create Popup: activity_timeline_popup
        this.layoutPopup = (LinearLayout) rootView.findViewById(R.id.popupLayout);
        this.textViewClose = (TextView) rootView.findViewById(R.id.txtclose);
        this.textLinearLayout = (LinearLayout) rootView.findViewById(R.id.textLayout);
        this.textViewChange = (TextView) rootView.findViewById(R.id.changeTimeline);
        this.radioButtonLinearLayout = (LinearLayout) rootView.findViewById(R.id.radioButtonsLayout);
        this.radioGroup = (RadioGroup) rootView.findViewById(R.id.radioButtonGroup);
        this.radioDay = (RadioButton) rootView.findViewById(R.id.radio_day);
        this.radioWeek = (RadioButton) rootView.findViewById(R.id.radio_week);
        this.radioMonth = (RadioButton) rootView.findViewById(R.id.radio_month);
        this.radioYear = (RadioButton) rootView.findViewById(R.id.radio_year);
        this.btnOk = (Button) rootView.findViewById(R.id.ok);

        //show only rootView without details or popup
        buttonAfter.setVisibility(View.INVISIBLE);

        greyBarDetailsLayout.setVisibility(View.INVISIBLE);
        backRootView.setVisibility(View.INVISIBLE);
        textBack.setVisibility(View.INVISIBLE);
        detailsLayoutLinear.setVisibility(View.INVISIBLE);
        drinkCategoryText.setVisibility(View.INVISIBLE);
        list_drinks.setVisibility(View.INVISIBLE);
        detailsTotalLayout.setVisibility(View.INVISIBLE);
        imageDetails.setVisibility(View.INVISIBLE);
        textViewTotal.setVisibility(View.INVISIBLE);

        layoutPopup.setVisibility(View.INVISIBLE);
        textViewClose.setVisibility(View.INVISIBLE);
        textLinearLayout.setVisibility(View.INVISIBLE);
        textViewChange.setVisibility(View.INVISIBLE);
        radioButtonLinearLayout.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        radioDay.setVisibility(View.INVISIBLE);
        radioWeek.setVisibility(View.INVISIBLE);
        radioMonth.setVisibility(View.INVISIBLE);
        radioYear.setVisibility(View.INVISIBLE);
        btnOk.setVisibility(View.INVISIBLE);

        //returns the view
        return rootView;
    }

    /**
     * shows the Calendar after the user has clicked the calendar-button
     * @param v
     */
    public void showCalendar(View v) {
        textViewDate.setText(date);
    }

    /**
     * change the Date to one day before the shown date
     * @param v - the view
     */
    public void changeDateOneDayBefore(View v) {
        Log.d(LOG_TAG, "changeDateOneDayBefore() enter");

        Calendar myCalendar = new GregorianCalendar();
        myCalendar.setTime(this.dateCalendar);
        myCalendar.add(Calendar.DATE, -1);
        this.date = this.formatter.format(myCalendar.getTime());
        this.dateCalendar = myCalendar.getTime();

        //checks if the day, week, month or year is shown
        if (timeline.equals("day")) {
            this.textViewDate.setText(this.date);
        } else if (timeline.equals("week")) {
            changeToWeek();
        } else if (timeline.equals("month")) {
            changeToMonth();
        } else if (timeline.equals("year")) {
            changeToYear();
        } else {
            this.textViewDate.setText(this.date);
        }

        //show button after only if its not today
        if (date.equals(actualDay)) {
            buttonAfter.setVisibility(View.GONE);
        } else {
            buttonAfter.setVisibility(View.VISIBLE);
        }
    }

    /**
     * change the Date to one day after the shown date
     * @param v - the view
     */
    public void changeDateOneDayAfter(View v) {
        Log.d(LOG_TAG, "changeDateOneDayAfter() enter");

        Calendar myCalendar = new GregorianCalendar();
        myCalendar.setTime(this.dateCalendar);
        myCalendar.add(Calendar.DATE, 1);
        this.date = this.formatter.format(myCalendar.getTime());
        this.dateCalendar = myCalendar.getTime();

        //checks if the day, week, month or year is shown
        if (timeline.equals("day")) {
            this.textViewDate.setText(this.date);
        } else if (timeline.equals("week")) {
            changeToWeek();
        } else if (timeline.equals("month")) {
            changeToMonth();
        } else if (timeline.equals("year")) {
            changeToYear();
        } else {
            this.textViewDate.setText(this.date);
        }

        //show button after only if its not today
        if (date.equals(actualDay)) {
            buttonAfter.setVisibility(View.GONE);
        } else {
            buttonAfter.setVisibility(View.VISIBLE);
        }
    }

    /**
     * sets the day when the page is initialized to today
     * @param item - the text view showing the date
     */
    public void setDate (TextView item){
        Date today = Calendar.getInstance().getTime();//getting date
        this.date = this.formatter.format(today);
        this.dateCalendar = today;
        this.actualDay = this.formatter.format(today);
        this.actualDayCalendar = today;
        item.setText(this.date);
    }

    /**
     * shows the Popup for changing the timeline after the user has clicked the timline-button
     * @param v - the view
     */
    public void changeTimeline(View v) {
        layoutPopup.setVisibility(View.VISIBLE);
        textViewClose.setVisibility(View.VISIBLE);
        textLinearLayout.setVisibility(View.VISIBLE);
        textViewChange.setVisibility(View.VISIBLE);
        radioButtonLinearLayout.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
        radioDay.setVisibility(View.VISIBLE);
        radioWeek.setVisibility(View.VISIBLE);
        radioMonth.setVisibility(View.VISIBLE);
        radioYear.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
    }

    /**
     * closes the popup if the user doesn't wont to change the timeline or after the click on the ok-button
     * @param v - the view
     */
    public void closePopup(View v) {
        layoutPopup.setVisibility(View.GONE);
        textViewClose.setVisibility(View.GONE);
        textLinearLayout.setVisibility(View.GONE);
        textViewChange.setVisibility(View.GONE);
        radioButtonLinearLayout.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
        radioDay.setVisibility(View.GONE);
        radioWeek.setVisibility(View.GONE);
        radioMonth.setVisibility(View.GONE);
        radioYear.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);
    }

    /**
     * checks if a radio button is chosen an changes the string timeline to the string of the chosen radio button
     * @param v - the view
     */
    public void onRadioButtonClicked(View v) {
        // Is the button checked?
        boolean checked = ((RadioButton) v).isChecked();

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
        }
    }

    /**
     * checks if the String timeline has a value and then goes to the method of the chosen radio button and closes the popup
     * @param v - the view
     */
    public void confirmPopup(View v) {
        boolean chosen = timeline.isEmpty();

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
        }
       closePopup(v);
    }

    /**
     * change timeline to day
     */
    public void changeToDay() {
        this.textViewDate.setText(this.date);
    }

    /**
     * change timeline to week
     */
    public void changeToWeek() {
        Calendar myCalendar = new GregorianCalendar();
        myCalendar.setTime(this.dateCalendar);
        myCalendar.add(Calendar.DATE, -7);
        this.weekDate = this.formatter.format(myCalendar.getTime());
        this.weekDateCalendar = myCalendar.getTime();

        String s = weekDate + " - " + this.date;
        this.textViewDate.setText(s);
    }

    /**
     * change timeline to month
     */
    public void changeToMonth() {
        Calendar myCalendar = new GregorianCalendar();
        myCalendar.setTime(this.dateCalendar);
        myCalendar.add(Calendar.MONTH, -1);
        this.monthDate = this.formatter.format(myCalendar.getTime());
        this.monthDateCalendar = myCalendar.getTime();

        String s = monthDate + " - " + this.date;
        this.textViewDate.setText(s);
    }

    /**
     * change timeline to year
     */
    public void changeToYear() {
        Calendar myCalendar = new GregorianCalendar();
        myCalendar.setTime(this.dateCalendar);
        myCalendar.add(Calendar.YEAR,-1);
        this.yearDate = this.formatter.format(myCalendar.getTime());
        this.yearDateCalendar = myCalendar.getTime();

        String s = yearDate + " - " + this.date;
        this.textViewDate.setText(s);
    }

    /**
     * sets the pie Chart
     * @param pieChart - the generetad pie Chart
     */
    public void setPieChart (PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        pieChart.setTouchEnabled(true);
        pieChart.setOnChartValueSelectedListener(this);

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(20f, 0));
        yvalues.add(new Entry(5f, 1));
        yvalues.add(new Entry(2f, 2));
        yvalues.add(new Entry(3f, 3));

        this.dataSet = new PieDataSet(yvalues, "");
        //PieDataSet dataSet = new PieDataSet(yvalues, "Getränkehaushalt");
        this.xVals = new ArrayList<String>();
        this.xVals.add("Ungesüsste Getränke");
        this.xVals.add("Sonstige Getränke");
        this.xVals.add("Koffeinhaltige Getränke");
        this.xVals.add("Alkoholhaltige Getränke");

        PieData data = new PieData(this.xVals, this.dataSet);

        // In percentage Term
        data.setValueFormatter(new PercentFormatter());

        //set Data
        pieChart.setData(data);

        //set Colors
        int [] color={ Color.rgb(50,205,50), Color.rgb(255,160,122), Color.rgb(238,174,238),
                Color.rgb(150,205,205)
        };
        dataSet.setColors(color);

        //Disable Hole in the Pie Chart
        pieChart.setDrawHoleEnabled(false);

        //Disable Drink category in the Pie Chart
        pieChart.setDrawSliceText(false);

        //Text Size and Text Color
        data.setValueTextSize(17f);
        data.setValueTextColor(Color.DKGRAY);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //pieChart.isDrawCenterTextEnabled();
        //pieChart.isUsePercentValuesEnabled();
        //pieChart.setUsePercentValues(true);
        //dataSet.

        //legend
        Legend l = pieChart.getLegend();
        l.setPosition(LegendPosition.ABOVE_CHART_CENTER);
        //l.setFormSize(17f);
        //l.setXEntrySpace(7);
        //l.setYEntrySpace(5);

        pieChart.invalidate(); // refresh
    }

    /**
     * closes the root View and shows the detail View
     */
    public void showDetails() {
        pieChartLinearLayout.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);
        //errorMessage.setVisibility(View.INVISIBLE);
        greyBarDetailsLayout.setVisibility(View.VISIBLE);
        backRootView.setVisibility(View.VISIBLE);
        textBack.setVisibility(View.VISIBLE);
        detailsLayoutLinear.setVisibility(View.VISIBLE);
        drinkCategoryText.setVisibility(View.VISIBLE);
        list_drinks.setVisibility(View.VISIBLE);
        detailsTotalLayout.setVisibility(View.VISIBLE);
        imageDetails.setVisibility(View.VISIBLE);
        textViewTotal.setVisibility(View.VISIBLE);
    }

    /**
     * closes the detail View and shows the root View
     * @param v - the view
     */
    public void goBack(View v) {
        greyBarDetailsLayout.setVisibility(View.GONE);
        backRootView.setVisibility(View.GONE);
        textBack.setVisibility(View.GONE);
        detailsLayoutLinear.setVisibility(View.GONE);
        drinkCategoryText.setVisibility(View.GONE);
        list_drinks.setVisibility(View.GONE);
        detailsTotalLayout.setVisibility(View.GONE);
        imageDetails.setVisibility(View.GONE);
        textViewTotal.setVisibility(View.GONE);

        pieChartLinearLayout.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.VISIBLE);
    }

    /**
     * sets the String of the text in the detail View
     * @param categoryText - the shown text with the total, the drink category and the date
     */
    public void totalText(String categoryText) {
        textViewTotal.setText("Du hast am "
                + date //day
                + " insgesamt "
                + 6 //total
                + "dl "
                + categoryText //category
                + " getrunken.");
    }

    /**
     * changes the image in the detail View to the chosen drink category
     * @param category - the chosen drink category
     */
    public void changeImage(int category) {
        if (category == 0) {
            imageDetails.setImageResource(R.drawable.image_water);
        } else if (category == 1) {
            imageDetails.setImageResource(R.drawable.image_soda);
        } else if (category == 2) {
            imageDetails.setImageResource(R.drawable.image_coffee);
        } else if (category == 3) {
            imageDetails.setImageResource(R.drawable.image_beer);
        } else {
            imageDetails.setImageResource(R.drawable.image_water);
        }
    }

    /**
     * Called when a value has been selected inside the chart.
     *
     * @param e The selected Entry.
     * @param h The corresponding highlight object that contains information
     * about the highlighted position
     */
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        showDetails();
        int index = h.getXIndex();
        this.drinkCategoryString = xVals.get(index);
        drinkCategoryText.setText(this.drinkCategoryString);
        totalText(this.drinkCategoryString);
        changeImage(index);
        //set listView --> data from database
    }

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    @Override
    public void onNothingSelected() {

    }
}
