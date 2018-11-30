package ch.bfh.btx8108.trinkster;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ch.bfh.btx8108.trinkster.database.DbHelper;
import ch.bfh.btx8108.trinkster.database.dao.DrinkDAO;

/**
 * Fragment zur Darstellung der History
 */
public class History extends Fragment {

    private static final String LOG_TAG = History.class.getSimpleName();

    private DrinkDAO drinkDAO;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_history, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        textView.setText( "HISTORY-SCREEN" );

        // prepare FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DbHelper dbHelper = new DbHelper(getContext());
        drinkDAO = new DrinkDAO(dbHelper);
        showAllListEntries(rootView);

        dbHelper.close();

        return rootView;
    }

    private void showAllListEntries (View rootView) {
        List<Drink> drinkList = drinkDAO.getAllDrinks();

        ArrayAdapter<Drink> drinkArrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_multiple_choice,
                drinkList);

        ListView drinkListView = (ListView) rootView.findViewById(R.id.listview_drinks);

        drinkListView.setBackgroundColor(Color.argb(255, 112, 128, 144));
        drinkListView.setAdapter(drinkArrayAdapter);
    }
}
