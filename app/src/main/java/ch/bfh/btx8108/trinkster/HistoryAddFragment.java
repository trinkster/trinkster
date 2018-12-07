package ch.bfh.btx8108.trinkster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class HistoryAddFragment extends Fragment {

    private static final String LOG_TAG = HistoryAddFragment.class.getSimpleName();

    static HistoryFragmentListener historyFragmentListener;

    public HistoryAddFragment() {

    }

    public static HistoryAddFragment createInstance(HistoryFragmentListener listener) {
        HistoryAddFragment historyAddFragment = new HistoryAddFragment();
        historyAddFragment.historyFragmentListener = listener;
        return historyAddFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView(): enter");

        View rootView = inflater.inflate(R.layout.activity_history_add, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        textView.setText( "Neues Getränk hinzufügen" );

        return rootView;
    }

    public void backPressed() {
        Log.d(LOG_TAG, "backPressed(): enter");
        historyFragmentListener.onSwitchToNextFragment();
    }

}
