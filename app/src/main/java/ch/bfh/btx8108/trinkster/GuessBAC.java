package ch.bfh.btx8108.trinkster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GuessBAC extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_guess_bac, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText( "GUESS YOUR BAC");
        return rootView;
    }

}
