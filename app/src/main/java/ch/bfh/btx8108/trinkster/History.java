package ch.bfh.btx8108.trinkster;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import ch.bfh.btx8108.trinkster.database.DbHelper;
import ch.bfh.btx8108.trinkster.database.dao.DrinkDAO;

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

        DbHelper dbHelper = new DbHelper(getContext());
        drinkDAO = new DrinkDAO(dbHelper);
        showAllListEntries(rootView);

        dbHelper.close();

        return rootView;
    }

    private void showAllListEntries (View rootView) {
//        List drinkList = drinkDAO.getAllDrinks();
        List<Object> drinkListDates = drinkDAO.getAllDrinksWithDates();

        ListView drinkListView = (ListView) rootView.findViewById(R.id.listview_drinks);

        drinkListView.setAdapter( new ArrayAdapter<Object>(getContext(), R.layout.activity_history_drinklist, R.id.drinklist_text1, drinkListDates ){
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
                if (getItem(position) instanceof Drink) {
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
                mFieldId = R.id.drinklist_text1;
                mContext = getContext();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                int type = getItemViewType(position);


                if (convertView == null) {
                    switch (type) {
                        case TYPE_DIVIDER:
                            row = inflater.inflate(R.layout.activity_history_drinklist_dateheader, parent, false);
                            break;
                        case TYPE_DRINK:
                            row = inflater.inflate(R.layout.activity_history_drinklist, parent, false);
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
//                        LocalDate parsedDate = LocalDate.parse(titleString, formatter);
                        tv1.setText(titleString);
                        break;
                    case TYPE_DRINK:
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
                        tv.setText(item.toString() + " dl");

                        // set sub text (Date and Time the drink was added)
                        TextView tv2 = row.findViewById(R.id.drinklist_text2);
                        LocalDateTime ldt1 = item.getDateTime();
                        DateTimeFormatter listDateFormatter = DateTimeFormatter.ofPattern("dd.MM.YYYY", new Locale("de-CH"));
                        DateTimeFormatter listTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", new Locale("de-CH"));
                        String drinkHistoryEntry = ldt1.format(listDateFormatter) + " um " + ldt1.format(listTimeFormatter) + " Uhr";
                        tv2.setText(drinkHistoryEntry);

                        // set info link according to category
                        ImageButton imgButton = row.findViewById(R.id.drinklist_icon);

                        break;
                }
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
