package ch.bfh.btx8108.trinkster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

public class StatisticDetails extends Fragment {
    private static final String LOG_TAG = StatisticDetails.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView() enter");

        View rootView = inflater.inflate(R.layout.activity_statistic_details, container, false);



        return rootView;
    }

}
