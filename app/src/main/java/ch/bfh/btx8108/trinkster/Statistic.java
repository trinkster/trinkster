package ch.bfh.btx8108.trinkster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import ch.bfh.btx8108.trinkster.database.DbHelper;
import ch.bfh.btx8108.trinkster.database.dao.StatisticDAO;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class Statistic extends Fragment implements OnChartValueSelectedListener {
    private static final String LOG_TAG = Statistic.class.getSimpleName();

    //ArrayList
    ArrayList<String> xVals;
    //Alert Dialog
    private AlertDialog.Builder builder;
    //Date
    Date actualDayCalendar;
    Date dateCalendar;
    Date monthDateCalendar;
    Date weekDateCalendar;
    Date yearDateCalendar;
    Date myCalendar;
    //EditText
    EditText txtDate;
    //Entry
    Entry entry;
    //Highlight
    Highlight highlight;
    //ImageButton
    ImageButton backRootView;
    ImageButton buttonAfter;
    ImageButton buttonBefore;
    ImageButton buttonCalendar;
    ImageButton buttonTimeline;
    //ImageView
    ImageView imageDetails;
    //int
    int different;
    int index;
    //LinearLayout
    LinearLayout detailsLayoutLinear;
    LinearLayout detailsTotalLayout;
    LinearLayout greyBarDetailsLayout;
    LinearLayout pieChartLinearLayout;
    //ListView
    ListView list_drinks;
    //long
    long between;
    //PieChart
    PieChart pieChart;
    //PieDataSet
    PieDataSet dataSet;
    //SimpleDateFormat
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy"); //formatting according to my need
    //String
    String actualDay;
    String date;
    String drinkCategoryString;
    String monthDate;
    String timeline = "day";
    String weekDate;
    String yearDate;
    String myDate;
    //TextView
    TextView drinkCategoryText;
    TextView errorMessage;
    TextView textBack;
    TextView textPieChart;
    TextView textViewDate;
    TextView textViewTotal;
    //View
    View rootView;

    /**
     * creates the statistic view
     * @param inflater - inflater for view
     * @param container - container for view
     * @param savedInstanceState - savedInstanceState for view
     * @return the view
     */
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
        setPieChart(pieChart);
        this.textPieChart = (TextView) rootView.findViewById(R.id.noPieChart);

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

        //show only rootView without details or popup
        buttonAfter.setVisibility(View.INVISIBLE);
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

        //returns the view
        return rootView;
    }

    /**
     * checks if the pie Chart is empty or not
     * and if empty shows a text
     * else shows the pie Chart
     */
    public void checkPieChart(){
        Log.d(LOG_TAG, "checkPieChart() enter");
        setPieChart(pieChart);

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
        Log.d(LOG_TAG, "showCalendar() enter");

        SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback() {

            //after click on button "cancel" -> do no changes
            @Override
            public void onCancelled() {
                Log.d(LOG_TAG, "onCancelled: calendar dialog cancelled");
            }

            //after click on button "save" -> show chosen date with pie Chart
            @Override
            public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
                Log.d(LOG_TAG, "onDataSelected: calendar dialog saved");

                //checks that the chosen date is not in the future
                Calendar thisCalendar = new GregorianCalendar();
                thisCalendar.setTime(actualDayCalendar);
                if (firstDate.after(thisCalendar)) {
                    Log.d(LOG_TAG, "firstDate.after(actualDayCalendar): true");
                    firstDate = thisCalendar;
                }
                if(secondDate!=null){
                    if (secondDate.after(thisCalendar)){
                        secondDate = thisCalendar;
                    }
                    if(firstDate==secondDate){
                        secondDate=null;
                    }
                }

                //changes the timeline after chosen date(s) in calendar
                if(secondDate==null){
                    timeline="day";
                    dateCalendar = firstDate.getTime();
                    date = formatter.format(dateCalendar);
                    changeToDay();
                } else {
                    myCalendar = firstDate.getTime();
                    myDate = formatter.format(myCalendar);
                    dateCalendar = secondDate.getTime();
                    date = formatter.format(dateCalendar);
                    String s = myDate + " - " + date;
                    setTextViewDate(s);
                    timeline = "different";
                    checkPieChart();
                    between = secondDate.getTimeInMillis()-firstDate.getTimeInMillis();
                    different = (int) (between / (1000*60*60*24));
                }

                checkTimeline();
                checkButtonAfter();
                checkPieChart();
            }
        };

        Date iDate = dateCalendar;
        if (timeline.equals("day")){
            iDate=dateCalendar;
        } else if (timeline.equals("week")){
            iDate=weekDateCalendar;
        } else if (timeline.equals("month")){
            iDate=monthDateCalendar;
        } else if (timeline.equals("year")){
            iDate=yearDateCalendar;
        } else{
            iDate=myCalendar;
        }

        new SlyCalendarDialog()
                .setSingle(false)
                .setCallback(callback)
                .setEndDate(dateCalendar)
                .setStartDate(iDate)
                .show(getActivity().getSupportFragmentManager(), "TAG_SLYCALENDAR");


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

    public void setTextViewDate(String s){
        this.textViewDate.setText(s);
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
            changeDifferentTimeline();
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
        } else if (timeline.equals("week")){
            myCalendar.add(Calendar.DATE, -7);
        } else if (timeline.equals("month")){
            myCalendar.add(Calendar.MONTH, -1);
        } else if (timeline.equals("year")){
            myCalendar.add(Calendar.YEAR, -1);
        } else {
            myCalendar.add(Calendar.DATE, -different);
            myCalendar.add(Calendar.DATE, -1);
        }
        this.date = this.formatter.format(myCalendar.getTime());
        this.dateCalendar = myCalendar.getTime();

        checkTimeline();
        checkButtonAfter();
        checkPieChart();
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
        } else if (timeline.equals("week")){
            myCalendar.add(Calendar.DATE, 7);
        } else if (timeline.equals("month")){
            myCalendar.add(Calendar.MONTH, 1);
        } else if (timeline.equals("year")){
            myCalendar.add(Calendar.YEAR, 1);
        } else {
            myCalendar.add(Calendar.DATE, different);
            myCalendar.add(Calendar.DATE, 1);
        }
        this.date = this.formatter.format(myCalendar.getTime());
        this.dateCalendar = myCalendar.getTime();
        if (date.compareTo(actualDay)>0){
            this.date = actualDay;
            this.dateCalendar = actualDayCalendar;
        }

        checkTimeline();
        checkButtonAfter();
        checkPieChart();
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
        this.builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

        this.builder.setIcon(R.drawable.ic_timeline_black_24dp);
        this.builder.setTitle(R.string.TEXT_CHANGE_TIMELINE);

        final String[] items = {"Tag", "Woche", "Monat", "Jahr"};
        int checkedItem = 4;

        if (timeline.equals("day")){
            checkedItem = 0;
        } else if (timeline.equals("week")){
            checkedItem = 1;
        } else if (timeline.equals("month")){
            checkedItem = 2;
        } else if (timeline.equals("year")){
            checkedItem = 3;
        }

        final String[] str = new String[1];
        str[0] = timeline;
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                if (items[which].equals("Tag")) {
                    str[0] = "day";
                    Log.d(LOG_TAG, "str day: " + str);
                }
                else if (items[which].equals("Woche")) {
                    str[0] = "week";
                    Log.d(LOG_TAG, "str week: " + str);
                }
                else if (items[which].equals("Monat")) {
                    str[0] = "month";
                }
                else if (items[which].equals("Jahr")) {
                    str[0] = "year";
                }
            }
        });

        builder.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                if (str[0].equals("day")){
                    timeline = "day";
                    changeToDay();
                } else if (str[0].equals("week")){
                    timeline = "week";
                    changeToWeek();
                } else if (str[0].equals("month")){
                    timeline = "month";
                    changeToMonth();
                } else if (str[0].equals("year")){
                    timeline = "year";
                    changeToYear();
                } else {
                    timeline = "day";
                    changeToDay();
                }
                checkPieChart();
            }
        });
        builder.setNegativeButton("Abbrechen", null);

        // create and show the alert dialog
        builder.show();
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
     * change timeline after chosen in calendar
     */
    public void changeDifferentTimeline(){
        Calendar thisCalendar = new GregorianCalendar();
        thisCalendar.setTime(dateCalendar);
        thisCalendar.add(Calendar.DATE, -different);
        this.myDate = this.formatter.format(thisCalendar.getTime());
        this.myCalendar = thisCalendar.getTime();

        String s = myDate + " - " + this.date;
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

        //get data from database
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
            LocalDateTime localDateMonth = setLocalTime(monthDateCalendar);
            statistic = statisticDAO.totalQuantitiesPerCategoryAndDay(localDateMonth, localDateTime);
        } else if (timeline.equals("year")) {
            LocalDateTime localDateYear = setLocalTime(yearDateCalendar);
            statistic = statisticDAO.totalQuantitiesPerCategoryAndDay(localDateYear, localDateTime);
        } else {
            LocalDateTime myLocalDateTime = setLocalTime(myCalendar);
            statistic = statisticDAO.totalQuantitiesPerCategoryAndDay(myLocalDateTime, localDateTime);
        }

        dbHelper.close();

        // set data
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
        int [] color={ Color.parseColor("#feb729"), Color.parseColor("#643173"), Color.parseColor("#2c6d6c"),
                Color.parseColor("#e2711d")
        };
        dataSet.setColors(color);

        //Disable Hole in the Pie Chart
        pieChart.setDrawHoleEnabled(false);

        //Disable Drink category in the Pie Chart
        pieChart.setDrawSliceText(false);

       // Enable description on bottom right of the chart
        pieChart.setDescription(null);

        //Text Size and Text Color
        data.setValueTextSize(24f);
        data.setValueTextColor(Color.WHITE);

        //legend
        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(18);
        l.setTextColor(Color.DKGRAY);
        l.setWordWrapEnabled(true);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(18);
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        Log.d(LOG_TAG, "Legend: " + l);
        l.resetCustom();

        // refresh
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    /**
     * closes the root View and shows the detail View
     */
    public void showDetails() {
        pieChartLinearLayout.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);
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
        List<Object> drinkList;

        if (timeline.equals("day")){
            drinkList = statisticDAO.getDrinksOfCategoryAndDate(category, localDateTime, localDateTime);
        } else if (timeline.equals("week")){
            LocalDateTime weekLocalDate = setLocalTime(weekDateCalendar);
            drinkList = statisticDAO.getDrinksOfCategoryAndDate(category, weekLocalDate, localDateTime);
        } else if (timeline.equals("month")){
            LocalDateTime monthLocalDate = setLocalTime(monthDateCalendar);
            drinkList = statisticDAO.getDrinksOfCategoryAndDate(category, monthLocalDate, localDateTime);
        } else if (timeline.equals("year")){
            LocalDateTime yearLocalDate = setLocalTime(yearDateCalendar);
            drinkList = statisticDAO.getDrinksOfCategoryAndDate(category, yearLocalDate, localDateTime);
        } else {
            LocalDateTime myLocalDateTime = setLocalTime(myCalendar);
            drinkList = statisticDAO.getDrinksOfCategoryAndDate(category, myLocalDateTime, localDateTime);
        }

        list_drinks.setAdapter( new ArrayAdapter<Object>(getContext(), R.layout.activity_statistic_drinklist, R.id.drinklist_text, drinkList){
                                      private static final int TYPE_DIVIDER = 0;
                                      private static final int TYPE_DRINK = 1;
                                      private final String LOG_TAG = ArrayAdapter.class.getSimpleName();

            private int mFieldId;
            private Context mContext;
            private View row;

            @Override
            public int getViewTypeCount() { // TYPE_DRINK and TYPE_DIVIDER
                return 2;
            }

            @Override
            public int getItemViewType(int position) {
                if (getItem(position) instanceof DrinkName) {
                    return TYPE_DRINK;
                }

                return TYPE_DIVIDER;
            }

            @Override
            public boolean isEnabled(int position) {
                return (getItemViewType(position) == TYPE_DRINK);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                mFieldId = R.id.drinklist_text;
                mContext = getContext();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                int type = getItemViewType(position);


                if (convertView == null) {
                    switch (type) {
                        case TYPE_DIVIDER:
                            row = inflater.inflate(R.layout.activity_history_drinklist_dateheader, parent, false);
                            break;
                        case TYPE_DRINK:
                            row = inflater.inflate(R.layout.activity_statistic_drinklist, parent, false);
                            break;
                    }
                } else {
                    row = convertView;
                }

                switch (type) {
                    case TYPE_DIVIDER:
                        LocalDate tmpDate = (LocalDate) getItem(position);

                        TextView tv1 = row.findViewById(R.id.separator);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("eeee, dd.MM.YYYY", new Locale("de-CH"));
                        String titleString = tmpDate.format(formatter);
                        tv1.setText(titleString);
                        break;
                    case TYPE_DRINK:
                        DrinkName item = (DrinkName) getItem(position);

                        // set image according to drink category
                        ImageView imgView = (ImageView) row.findViewById(R.id.drinklist_drink_image);
                        switch(category){
                            case "Ungesüsste Getränke":
                                imgView.setImageDrawable(getResources().getDrawable(R.drawable.image_water));
                                break;
                            case "Sonstige Getränke":
                                imgView.setImageDrawable(getResources().getDrawable(R.drawable.image_soda));
                                break;
                            case "Koffeinhaltige Getränke":
                                imgView.setImageDrawable(getResources().getDrawable(R.drawable.image_coffee));
                                break;
                            case "Alkoholhaltige Getränke":
                                imgView.setImageDrawable(getResources().getDrawable(R.drawable.image_beer));
                                break;
                            default:
                                imgView.setImageDrawable(getResources().getDrawable(R.drawable.image_water));
                                break;
                        }

                        // set main text (Drink name and its amount)
                        TextView tv = row.findViewById(R.id.drinklist_text);
                        tv.setText(item.toString() + " " + getString(R.string.TEXT_DL));

                        // set info link according to category
                        ImageButton imgButton = row.findViewById(R.id.drinklist_icon);

                        break;
                }
                return row;

            }});
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
        onNothingSelected();
    }

    /**
     * change date to LocalDateTime
     * @param dateLocal - Date
     * @return LocalDateTim
     */
    public LocalDateTime setLocalTime (Date dateLocal) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateLocal);
        return LocalDateTime.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,
                c.get(Calendar.DAY_OF_MONTH), 0, 0);
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

        //gets data from database
        if (timeline.equals("day")) {
            total = statisticDAO.totalQuantityForACategoryAndDay(categoryText, localDateTime, localDateTime);
        } else if (timeline.equals("week")) {
            LocalDateTime localDateWeek = setLocalTime(weekDateCalendar);
            total = statisticDAO.totalQuantityForACategoryAndDay(categoryText, localDateWeek, localDateTime);
        } else if (timeline.equals("month")) {
            LocalDateTime localDateMonth = setLocalTime(monthDateCalendar);
            total = statisticDAO.totalQuantityForACategoryAndDay(categoryText, localDateMonth, localDateTime);
        } else if (timeline.equals("year")) {
            LocalDateTime localDateYear = setLocalTime(yearDateCalendar);
            total = statisticDAO.totalQuantityForACategoryAndDay(categoryText, localDateYear, localDateTime);
        } else {
            LocalDateTime myLocalDateTime = setLocalTime(myCalendar);
            total = statisticDAO.totalQuantityForACategoryAndDay(categoryText, myLocalDateTime, localDateTime);
        }

        dbHelper.close();

        textViewTotal.setText(total + " " + getString(R.string.TEXT_DL));
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
        pieChart.highlightValues(null);
    }

}
