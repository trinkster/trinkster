package ch.bfh.btx8108.trinkster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;

public class Statistic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(8f, 0));
        yvalues.add(new Entry(15f, 1));
        yvalues.add(new Entry(12f, 2));
        yvalues.add(new Entry(25f, 3));
        yvalues.add(new Entry(23f, 4));
        yvalues.add(new Entry(17f, 5));

        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("January");
        xVals.add("February");
        xVals.add("March");
        xVals.add("April");
        xVals.add("May");
        xVals.add("June");

        PieData data = new PieData(xVals, dataSet);

        // In percentage Term
        data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));

        pieChart.setData(data);
    }
}
