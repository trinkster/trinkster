package ch.bfh.btx8108.trinkster;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.time.LocalDateTime;

import ch.bfh.btx8108.trinkster.database.DbHelper;
import ch.bfh.btx8108.trinkster.database.dao.DrinkDAO;
import ch.bfh.btx8108.trinkster.HistoryAddFragment;

/**
 * Fragment zur Darstellung der History
 */
public class History extends Fragment{

    private static final String LOG_TAG = History.class.getSimpleName();

    private DrinkDAO drinkDAO;
    private Context context;

    static HistoryFragmentListener historyFragmentListener;

    public History() {

    }

    public static History createInstance(HistoryFragmentListener listener) {
        History historyFragment = new History();
        historyFragment.historyFragmentListener = listener;
        return historyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_history, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        textView.setText( "HISTORY-SCREEN" );

        DbHelper dbHelper = new DbHelper(getContext());
        drinkDAO = new DrinkDAO(dbHelper);
        showAllListEntries(rootView);

        dbHelper.close();

        return rootView;
    }

    private void showAllListEntries (View rootView) {
        List<Drink> drinkList = drinkDAO.getAllDrinks();
        ListView drinkListView = (ListView) rootView.findViewById(R.id.listview_drinks);

        drinkListView.setAdapter( new ArrayAdapter<Drink>(getContext(), R.layout.activity_history_drinklist, R.id.drinklist_text1, drinkList ){
             @Override
             public View getView(int position, View convertView, ViewGroup parent) {
                 View row = super.getView(position, convertView, parent);
                 Drink item = (Drink) getItem(position);

                 // set image according to drink category
                 ImageView imgView = (ImageView) row.findViewById(R.id.drinklist_drink_image);
                 switch((int) item.getCategory().getId()){
                     case 1:
                         imgView.setImageDrawable(getResources().getDrawable(R.drawable.image_water));
                         break;
                     case 2:
                         imgView.setImageDrawable(getResources().getDrawable(R.drawable.image_soda));
                         break;
                     case 3:
                         imgView.setImageDrawable(getResources().getDrawable(R.drawable.image_coffee));
                         break;
                     case 4:
                         imgView.setImageDrawable(getResources().getDrawable(R.drawable.image_beer));
                         break;
                     default:
                         imgView.setImageDrawable(getResources().getDrawable(R.drawable.image_water));
                         break;
                 }

                 // set main text (Drink name and its amount)
                 TextView tv = row.findViewById(R.id.drinklist_text1);
                 tv.setText(item.toString() + "dl");

                 // set sub text (Date and Time the drink was added)
                 TextView tv2 = row.findViewById(R.id.drinklist_text2);
                 LocalDateTime ldt1 = item.getDateTime();
                 tv2.setText(ldt1.getDayOfMonth() +"."+ldt1.getMonthValue() + "." +ldt1.getYear() + " um " + ldt1.toLocalTime());

                 // set info link accodrding to category
                 ImageButton imgButton = row.findViewById(R.id.drinklist_icon);

                 return row;
             }});
    }

    /**
     * This method is called when the FAB-button is pressed
     * It basically just redirects from the current History-Fragment to the next HistoryAdd-Fragment.
     *
     * @param rootView
     */
    public void addDrink(View rootView){
        Log.d(LOG_TAG, "addDrink(): enter");


        Log.d(LOG_TAG, "addDrink(): SWITCH TO NEXT HISTORYADD-VIEW");
        historyFragmentListener.onSwitchToNextFragment();
    }

}
