package ch.bfh.btx8108.trinkster;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ch.bfh.btx8108.trinkster.database.DbHelper;
import ch.bfh.btx8108.trinkster.database.dao.DrinkDAO;
import ch.bfh.btx8108.trinkster.database.dao.StatisticDAO;

public class Statistic extends Fragment implements OnChartValueSelectedListener, OnDateChangeListener {
    private static final String LOG_TAG = Statistic.class.getSimpleName();

    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy"); //formatting according to my need
    View rootView;
    ListView list_drinks;
    PieChart pieChart;
    ImageView imageDetails;
    PieDataSet dataSet;
    ArrayList<String> xVals;
    CalendarView simpleCalendarViewLayout;
    private DrinkDAO drinkDAO;

    //Button
    Button btnOk;
    Button okCalendarBtn;

    //Date
    Date actualDayCalendar;
    Date dateCalendar;
    Date monthDateCalendar;
    Date weekDateCalendar;
    Date yearDateCalendar;

    //ImageButton
    ImageButton backRootView;
    ImageButton buttonAfter;
    ImageButton buttonBefore;
    ImageButton buttonCalendar;
    ImageButton buttonTimeline;

    //LinearLayout
    LinearLayout calendarLinearLayout;
    LinearLayout detailsLayoutLinear;
    LinearLayout detailsTotalLayout;
    LinearLayout greyBarDetailsLayout;
    LinearLayout greyBarRootLayout;
    LinearLayout layoutPopup;
    LinearLayout pieChartLinearLayout;
    LinearLayout radioButtonLinearLayout;
    LinearLayout textLinearLayout;

    //RadioGroup
    RadioGroup radioGroup;
    RadioButton radioDay;
    RadioButton radioWeek;
    RadioButton radioMonth;
    RadioButton radioYear;

    //String
    String actualDay;
    String date;
    String drinkCategoryString;
    String monthDate;
    String timeline = "day";
    String weekDate;
    String yearDate;

    //TextView
    TextView drinkCategoryText;
    TextView errorMessage;
    TextView textBack;
    TextView textPieChart;
    TextView textViewChange;
    TextView textViewClose;
    TextView textViewDate;
    TextView textViewTotal;

    Entry entry;
    int index;
    Highlight highlight;

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
        this.buttonBefore = rootView.findViewById(R.id.dayBefore);
        this.buttonAfter =(ImageButton)rootView.findViewById(R.id.dayAfter);
        this.buttonTimeline = (ImageButton) rootView.findViewById(R.id.timeline);
        this.pieChartLinearLayout = (LinearLayout) rootView.findViewById(R.id.pieChartLayout);
        this.pieChart = (PieChart) rootView.findViewById(R.id.piechart);
        //setPieChart(pieChart);
        this.textPieChart = (TextView) rootView.findViewById(R.id.noPieChart);

        this.errorMessage = (TextView) rootView.findViewById(R.id.error_message);

        //create calendarView
        this.calendarLinearLayout = (LinearLayout) rootView.findViewById(R.id.calendarLayout);
        this.simpleCalendarViewLayout =(CalendarView) rootView.findViewById(R.id.simpleCalendarView);
        this.okCalendarBtn = (Button) rootView.findViewById(R.id.okCalendar);

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

        //create database
        DbHelper dbHelper = new DbHelper(getContext());
        this.drinkDAO = new DrinkDAO(dbHelper);

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
        calendarLinearLayout.setVisibility(View.GONE);
        simpleCalendarViewLayout.setVisibility(View.GONE);
        okCalendarBtn.setVisibility(View.GONE);
        checkPieChart();

        greyBarDetailsLayout.setVisibility(View.GONE);
        backRootView.setVisibility(View.GONE);
        textBack.setVisibility(View.GONE);
        detailsLayoutLinear.setVisibility(View.GONE);
        drinkCategoryText.setVisibility(View.GONE);
        list_drinks.setVisibility(View.GONE);
        detailsTotalLayout.setVisibility(View.GONE);
        imageDetails.setVisibility(View.GONE);
        textViewTotal.setVisibility(View.GONE);

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

        //dbHelper.close();

        //returns the view
        return rootView;
    }

    /**
     * checks if the pie Chart is empty or not
     */
    public void checkPieChart(){
        setPieChart(pieChart);
        //pieChart.invalidate(); // refresh
        this.pieChart.refreshDrawableState();

        boolean check = this.pieChart.isEmpty();
        Log.d(LOG_TAG, "checkPieChart() enter: " + check);

        if(check){
            this.pieChart.setVisibility(View.GONE);
            this.textPieChart.setVisibility(View.VISIBLE);
        } else {
            this.pieChart.setVisibility(View.VISIBLE);
            this.textPieChart.setVisibility(View.GONE);
        }
    }

    /**
     * shows the Calendar after the user has clicked the calendar-button
     * @param v - the view
     */
    public void showCalendar(View v) {
        textViewDate.setVisibility(View.GONE);
        buttonCalendar.setVisibility(View.GONE);
        buttonBefore.setVisibility(View.GONE);
        buttonAfter.setVisibility(View.GONE);
        buttonTimeline.setVisibility(View.GONE);
        pieChartLinearLayout.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);

        calendarLinearLayout.setVisibility(View.VISIBLE);
        simpleCalendarViewLayout.setVisibility(View.VISIBLE);
        okCalendarBtn.setVisibility(View.VISIBLE);

        long dateInLong = dateCalendar.getTime();

        if (timeline.equals("day")){
            simpleCalendarViewLayout.setDate(dateInLong, true, false);
        } else if (timeline.equals("week")){
            long weekInLong = weekDateCalendar.getTime();
            simpleCalendarViewLayout.setDate(weekInLong, true, false);
        } else if (timeline.equals("month")) {
            long monthInLong = monthDateCalendar.getTime();
        } else if (timeline.equals("year")){
            long yearInLong = yearDateCalendar.getTime();
        }

        simpleCalendarViewLayout.setMaxDate(actualDayCalendar.getTime());
        //simpleCalendarViewLayout.setMinDate();

        //simpleCalendarViewLayout.set
        simpleCalendarViewLayout.setOnDateChangeListener(this);
    }

    /**
     * closes the calendar
     */
    public void closeCalendar() {
        calendarLinearLayout.setVisibility(View.GONE);
        simpleCalendarViewLayout.setVisibility(View.GONE);
        okCalendarBtn.setVisibility(View.GONE);

        textViewDate.setVisibility(View.VISIBLE);
        buttonCalendar.setVisibility(View.VISIBLE);
        buttonBefore.setVisibility(View.VISIBLE);
        buttonAfter.setVisibility(View.VISIBLE);
        buttonTimeline.setVisibility(View.VISIBLE);
        pieChartLinearLayout.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.VISIBLE);
    }

    /**
     * closes the calender when the ok button was pressed and changes the date
     * @param v - the view
     */
    public void confirmCalendar(View v) {
        textViewDate.setText(this.date);
        closeCalendar();
        checkTimeline();
        checkButtonAfter();
        checkPieChart(); //setPieChart(this.pieChart);
    }

    /**
     * checks if the button after has to be shown
     */
    public void checkButtonAfter () {
        //show button after only if its not today
        if (date.equals(actualDay)) {
            buttonAfter.setVisibility(View.INVISIBLE);
        } else {
            buttonAfter.setVisibility(View.VISIBLE);
        }
    }

    /**
     * checks if the day, week, month or year is shown
     */
    public void checkTimeline () {
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
    }

    /**
     * change the Date to one day before the shown date
     * @param v - the view
     */
    public void changeDateOneDayBefore(View v) {
        Log.d(LOG_TAG, "changeDateOneDayBefore() enter");

        Calendar myCalendar = new GregorianCalendar();
        myCalendar.setTime(this.dateCalendar);

        if (timeline.equals("day")){
            myCalendar.add(Calendar.DATE, -1);
            this.date = this.formatter.format(myCalendar.getTime());
            this.dateCalendar = myCalendar.getTime();
        } else if (timeline.equals("week")){
            myCalendar.add(Calendar.DATE, -7);
            this.date = this.formatter.format(myCalendar.getTime());
            this.dateCalendar = myCalendar.getTime();
        } else if (timeline.equals("month")){
            myCalendar.add(Calendar.MONTH, -1);
            this.date = this.formatter.format(myCalendar.getTime());
            this.dateCalendar = myCalendar.getTime();
        } else if (timeline.equals("year")){
            myCalendar.add(Calendar.YEAR, -1);
            this.date = this.formatter.format(myCalendar.getTime());
            this.dateCalendar = myCalendar.getTime();
        } else {
            this.errorMessage.setText("Button changeDateBefore hat nicht funktioniert.");
        }

        checkTimeline();
        checkButtonAfter();
        checkPieChart();
      //setPieChart(this.pieChart);
    }

    /**
     * change the Date to one day after the shown date
     * @param v - the view
     */
    public void changeDateOneDayAfter(View v) {
        Log.d(LOG_TAG, "changeDateOneDayAfter() enter");

        Calendar myCalendar = new GregorianCalendar();
        myCalendar.setTime(this.dateCalendar);

        if (timeline.equals("day")){
            myCalendar.add(Calendar.DATE, 1);
            this.date = this.formatter.format(myCalendar.getTime());
            this.dateCalendar = myCalendar.getTime();
        } else if (timeline.equals("week")){
            myCalendar.add(Calendar.DATE, 7);
            this.date = this.formatter.format(myCalendar.getTime());
            this.dateCalendar = myCalendar.getTime();
        } else if (timeline.equals("month")){
            myCalendar.add(Calendar.MONTH, 1);
            this.date = this.formatter.format(myCalendar.getTime());
            this.dateCalendar = myCalendar.getTime();
        } else if (timeline.equals("year")){
            myCalendar.add(Calendar.YEAR, 1);
            this.date = this.formatter.format(myCalendar.getTime());
            this.dateCalendar = myCalendar.getTime();
        } else {
            this.errorMessage.setText("Button changeDateAfter hat nicht funktioniert.");
        }

        checkTimeline();
        checkButtonAfter();
        checkPieChart();     // setPieChart(this.pieChart);
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

        //toggle selected timeline
        if (timeline.equals("day")){
            radioDay.toggle();
        } else if (timeline.equals("week")){
            radioWeek.toggle();
        } else if (timeline.equals("month")){
            radioMonth.toggle();
        } else if (timeline.equals("year")){
            radioYear.toggle();
        } else {
            radioDay.toggle();
        }
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
        checkPieChart();   //setPieChart(pieChart);
    }

    /**
     * changes the date to the actual day
     * @param v - this view
     */
    public void thisDay(View v) {
        this.date = this.actualDay;
        this.dateCalendar = this.actualDayCalendar;
        this.timeline = "day";
        this.textViewDate.setText(this.date);
        closeCalendar();
        checkButtonAfter();
        setPieChart(this.pieChart);
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
        myCalendar.add(Calendar.DAY_OF_MONTH,1);
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
        myCalendar.add(Calendar.DAY_OF_MONTH,1);
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
        myCalendar.add(Calendar.DAY_OF_MONTH,1);
        this.yearDate = this.formatter.format(myCalendar.getTime());
        this.yearDateCalendar = myCalendar.getTime();

        String s = yearDate + " - " + this.date;
        this.textViewDate.setText(s);
    }

    /**
     * sets the pie Chart
     * @param pieChart - the generated pie Chart
     */
    public void setPieChart (PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        pieChart.setTouchEnabled(true);
        pieChart.setOnChartValueSelectedListener(this);
        //pieChart.setHighlightPerTapEnabled(false);

        DbHelper dbHelper = new DbHelper(getContext());
        StatisticDAO statisticDAO = new StatisticDAO(dbHelper);
        LocalDateTime localDateTime = setLocalTime(dateCalendar);
        List<StatisticEntry> statistic = statisticDAO.totalQuantitiesPerCategoryAndDay(localDateTime, localDateTime);

        if (timeline.equals("day")) {
            statistic = statisticDAO.totalQuantitiesPerCategoryAndDay(localDateTime, localDateTime);
        } else if (timeline.equals("week")) {
            LocalDateTime localDateWeek = setLocalTime(weekDateCalendar);
            statistic = statisticDAO.totalQuantitiesPerCategoryAndDay(localDateWeek, localDateTime);
        } else if (timeline.equals("month")) {
            LocalDateTime localDateMonth = setLocalTime(weekDateCalendar);
            statistic = statisticDAO.totalQuantitiesPerCategoryAndDay(localDateMonth, localDateTime);
        } else if (timeline.equals("year")) {
            LocalDateTime localDateYear = setLocalTime(weekDateCalendar);
            statistic = statisticDAO.totalQuantitiesPerCategoryAndDay(localDateYear, localDateTime);
        }

        dbHelper.close();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        this.xVals = new ArrayList<>();

        for (int i = 0; i < statistic.size(); i++) {
            yvalues.add(new Entry(statistic.get(i).getQuantity().floatValue(), i));
            this.xVals.add(statistic.get(i).getCategory());
        }

        this.dataSet = new PieDataSet(yvalues, "");
        PieData data = new PieData(this.xVals, this.dataSet);

        Log.d(LOG_TAG, "xVals: " + xVals);

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

       // Enable description on bottom right of the chart
        pieChart.setDescription(null);

        //Text Size and Text Color
        data.setValueTextSize(18f);
        data.setValueTextColor(Color.DKGRAY);

        //legend -> manuell setzen?
        Legend l = pieChart.getLegend();
      //l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(14f);
        l.setWordWrapEnabled(true);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(14f);
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        Log.d(LOG_TAG, "Legend: " + l);
        l.resetCustom();

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
     * shows the drinks grouped by name with quantity
     * @param category - the drink category
     */
    public void showEntries(String category) {
        DbHelper dbHelper = new DbHelper(getContext());
        StatisticDAO statisticDAO = new StatisticDAO(dbHelper);
        LocalDateTime localDateTime = setLocalTime(dateCalendar);
        List<DrinkName> drinkList = statisticDAO.getDrinksOfCategoryAndDate(category, localDateTime, localDateTime);


        ArrayAdapter<DrinkName> drinkArrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_multiple_choice,
                drinkList);

        Log.d(LOG_TAG, "Context: " + getContext());

        list_drinks.setBackgroundColor(Color.argb(255, 112, 128, 144));
        list_drinks.setAdapter(drinkArrayAdapter);
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
        //setPieChart(this.pieChart);
    }

    public LocalDateTime setLocalTime (Date dateLocal) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateLocal);
        int year = c.get(Calendar.YEAR);
        int monthJava = c.get(Calendar.MONTH);
        int month = monthJava + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        LocalDateTime localDate = LocalDateTime.of(year, month, day, 0, 0);
        return  localDate;
    }

    /**
     * sets the String of the text in the detail View
     * @param categoryText - the shown text with the total, the drink category and the date
     */
    public void totalText(String categoryText) {
        DbHelper dbHelper = new DbHelper(getContext());
        StatisticDAO statisticDAO = new StatisticDAO(dbHelper);
        LocalDateTime localDateTime = setLocalTime(dateCalendar);

        double total = 0;

        if (timeline.equals("day")) {
            total = statisticDAO.totalQuantityForACategoryAndDay(categoryText, localDateTime, localDateTime);
        } else if (timeline.equals("week")) {
            LocalDateTime localDateWeek = setLocalTime(weekDateCalendar);
            total = statisticDAO.totalQuantityForACategoryAndDay(categoryText, localDateWeek, localDateTime);
        } else if (timeline.equals("month")) {
            LocalDateTime localDateMonth = setLocalTime(weekDateCalendar);
            total = statisticDAO.totalQuantityForACategoryAndDay(categoryText, localDateMonth, localDateTime);
        } else if (timeline.equals("year")) {
            LocalDateTime localDateYear = setLocalTime(weekDateCalendar);
            total = statisticDAO.totalQuantityForACategoryAndDay(categoryText, localDateYear, localDateTime);
        }

        dbHelper.close();

        textViewTotal.setText("Du hast am "
                + date //day
                + " insgesamt \n"
                + total //total
                + " dl \n"
                + categoryText //category
                + " getrunken.");
    }

    /**
     * changes the image in the detail View to the chosen drink category
     * @param category - the chosen drink category
     */
    public void changeImage(String category) {
        if (category.equals("Ungesüsste Getränke")) {
            imageDetails.setImageResource(R.drawable.image_water);
        } else if (category.equals("Sonstige Getränke")) {
            imageDetails.setImageResource(R.drawable.image_soda);
        } else if (category.equals("Koffeinhaltige Getränke")) {
            imageDetails.setImageResource(R.drawable.image_coffee);
        } else if (category.equals("Alkoholhaltige Getränke")) {
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
        this.entry = e;
        this.index = dataSetIndex;
        this.highlight = h;
        showDetails();
        int Xindex = h.getXIndex();
        this.drinkCategoryString = xVals.get(Xindex);
        drinkCategoryText.setText(this.drinkCategoryString);
        totalText(this.drinkCategoryString);
        changeImage(this.drinkCategoryString);
        showEntries(this.drinkCategoryString);
    }

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    @Override
    public void onNothingSelected() {
        //pieChart.getOnChartGestureListener().
        //onValueSelected(entry, index, highlight);
        //pieChart.highlightValues(null);
        //pieChart.highlightTouch(null);
        pieChart.valuesToHighlight();
        pieChart.highlightValue(null);
        pieChart.setSelected(false);
        pieChart.getDefaultFocusHighlightEnabled();
        //pieChart.needsHighlight(highlight, 0);
    }

    /**
     * changes the date when the user clicks on an other day
     * @param view - the calendar view
     * @param year - the selected year
     * @param month - the selected month
     * @param dayOfMonth - the selected day of month
     */
    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        Calendar myCalendar = new GregorianCalendar();
        myCalendar.set(year, month, dayOfMonth);
        this.date = formatter.format(myCalendar.getTime());
        this.dateCalendar = myCalendar.getTime();
    }
}
