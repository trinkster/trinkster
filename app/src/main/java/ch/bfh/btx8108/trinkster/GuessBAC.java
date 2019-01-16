package ch.bfh.btx8108.trinkster;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import BACtrackAPI.API.BACtrackAPI;
import BACtrackAPI.API.BACtrackAPICallbacks;
import BACtrackAPI.Constants.BACTrackDeviceType;
import BACtrackAPI.Exceptions.BluetoothLENotSupportedException;
import BACtrackAPI.Exceptions.BluetoothNotEnabledException;
import BACtrackAPI.Exceptions.LocationServicesNotEnabledException;
import BACtrackAPI.Mobile.Constants.Errors;

/**
 * Fragment zustaendig fuer den Promillenschaetzer
 */
public class GuessBAC extends Fragment {
    private static final String LOG_TAG = GuessBAC.class.getSimpleName();
    private static final byte PERMISSIONS_FOR_SCAN = 100;

    private TextView statusMessageTextView;

    private String apiKey = "08ee704da98a4865b0fbf46117c4be";
    private BACtrackAPI mAPI;

    private Context mContext;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_guess_bac, container, false);

        this.statusMessageTextView = (TextView) rootView.findViewById(R.id.status_message_text_view_id);

        SeekBar simpleSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar); // initiate the Seek bar
        final TextView seekBarValue = rootView.findViewById(R.id.guessbac_number);
        final TextView seekBarDescr = rootView.findViewById(R.id.guessbac_number_descr);
        seekBarValue.setText( "0.0 " + getString(R.string.STR_BAC_IN_PROMILLE));
        seekBarDescr.setText(getString(R.string.PROMILLE_AB_00));

        simpleSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float bacInPromille = ((float) progress)/10;
                        String bac = String.valueOf(bacInPromille);

                        seekBarValue.setText( bac + " " + getString(R.string.STR_BAC_IN_PROMILLE));

                        switch (bac){
                            case "0.0":
                                seekBarDescr.setText(getString(R.string.PROMILLE_AB_00));
                                break;
                            case "0.1": case "0.2":
                                seekBarDescr.setText(getString(R.string.PROMILLE_AB_01));
                                break;
                            case "0.3": case "0.4":
                                seekBarDescr.setText(getString(R.string.PROMILLE_AB_03));
                                break;
                            case "0.5": case "0.6": case "0.7":
                                seekBarDescr.setText(getString(R.string.PROMILLE_AB_05));
                                break;
                            case "0.8": case "0.9":
                                seekBarDescr.setText(getString(R.string.PROMILLE_AB_08));
                                break;
                            case "1.0": case "1.1": case "1.2": case "1.3": case "1.4": case "1.5": case "1.6": case "1.7": case "1.8": case "1.9":
                                seekBarDescr.setText(getString(R.string.PROMILLE_AB_10));
                                break;
                            case "2.0": case "2.1": case "2.2": case "2.3": case "2.4": case "2.5": case "2.6": case "2.7": case "2.8": case "2.9":
                                seekBarDescr.setText(getString(R.string.PROMILLE_AB_20));
                                break;
                            case "3.0":
                                seekBarDescr.setText(getString(R.string.PROMILLE_AB_30));
                                break;
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }
                });

        prepareBACTrack();

        return rootView;
    }

    /**
     * Wird aufgerufen um einerseits zu pruefen ob die notwendigen Berechtigungen seitens des
     * Android-Geraetes gegeben sind und um andererseits die BACTrack-API zu initialisieren.
     */
    private void prepareBACTrack(){
        Log.v(LOG_TAG, "prepareBACTrack() enter");

        try {
            mAPI = new BACtrackAPI(this.getContext(), mCallbacks, apiKey);
            mContext = this.getContext();
        } catch (BluetoothLENotSupportedException e) {
            e.printStackTrace();
            this.setStatus(R.string.TEXT_ERR_BLE_NOT_SUPPORTED);
        } catch (BluetoothNotEnabledException e) {
            e.printStackTrace();
            this.setStatus(R.string.TEXT_ERR_BT_NOT_ENABLED);
        } catch (LocationServicesNotEnabledException e) {
            e.printStackTrace();
            this.setStatus(R.string.TEXT_ERR_LOCATIONS_NOT_ENABLED);
        }

        Log.v(LOG_TAG, "prepareBACTrack() leave");
    }

    /**
     * Wird aufgerufen wenn ein Breathalyzer-Vergleich gestartet wird.
     * Erstellt zugleich auch eine Breathalyzer-Verbindung falls noch keine vorhanden ist.
     * @param v
     */
    public void startBreathalyzerComparison(View v){
        Log.v(LOG_TAG, "startBreathalyzerComparison() enter");

        builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);

        if (mAPI != null) {
            // check if connected to a bactrack
            if(!mAPI.isConnected()){
                Log.d(LOG_TAG, "startBreathalyzerComparison(): not yet connected to the bactrack but trying to connect...");
                builder.setTitle(R.string.DIALOG_NO_BREATHALYZER_TITLE).setMessage(R.string.DIALOG_NO_BREATHALYZER_MESSAGE)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with connecting
                                setStatus(R.string.TEXT_CONNECTING);

                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_FOR_SCAN);
                                } else {
                                    Log.d(LOG_TAG, "startBreathalyzerComparison(): Permission already granted, start connecting.");
                                    mAPI.connectToNearestBreathalyzer();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info).show();
            } else {
                // already connected, prepare blowing process
                Log.d(LOG_TAG, "startBreathalyzerComparison() already connected, prepare blowing process");
                boolean result = mAPI.startCountdown();

                if (!result)
                    Log.e(LOG_TAG, "mAPI.startCountdown() failed");
                else
                    Log.d(LOG_TAG, "startBreathalyzerComparison() Blow process start requested");
            }
        }

        Log.v(LOG_TAG, "startBreathalyzerComparison() leave");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_FOR_SCAN: { // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /** Only start scan if permissions granted.  */
                    mAPI.connectToNearestBreathalyzer();
                }
            }
        }
    }

    private void setStatus(int resourceId) {
        this.setStatus(this.getResources().getString(resourceId));
    }

    // FIXME: remove this at the end
    private void setStatus(final String message) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusMessageTextView.setText(String.format("Status:\n%s", message));
            }
        });
    }

    private void showCountdownDialog(final int currentcountdown){
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.v(LOG_TAG, "showCountdownDialog() enter");
                Log.d(LOG_TAG, "showCountdownDialog() countdown: " + currentcountdown);

                if( dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }

                builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle(getString(R.string.DIALOG_BREATHALYZER_COUNTDOWN_TITLE));
                builder.setMessage(getString(R.string.DIALOG_BREATHALYZER_COUNTDOWN_MESSAGE) + " " + currentcountdown).create();

                dialog = builder.create();
                dialog.show();

            }
        });
    }

    private void showBlowingDialog(){
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.v(LOG_TAG, "showBlowingDialog() enter");

                if( dialog != null && dialog.isShowing()){
                    int titleId = getResources().getIdentifier( "alertTitle", "id", "android" );

                    // the following condition makes sure that the dialog is only shown once when blowing is requested
                    // this is needed because the communication between the breathalyzer and the app is async
                    // otherwise a few hunderds of dialog would pop up
                    // I know this is a dirty hack but I ain't got time for pretty stuff now.
                    if (titleId > 0) {
                        TextView dialogTitle = (TextView) dialog.findViewById(titleId);
                        if (dialogTitle != null) {
                            if(dialogTitle.getText().toString().equals(getString(R.string.DIALOG_BREATHALYZER_KEEPBLOWING_TITLE))){
                                Log.d(LOG_TAG, "showBlowingDialog(): already displaying the dialog, not going to display it again...");
                            } else {
                                dialog.dismiss();

                                builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                                builder.setTitle(getString(R.string.DIALOG_BREATHALYZER_KEEPBLOWING_TITLE));
                                builder.setMessage(getString(R.string.DIALOG_BREATHALYZER_KEEPBLOWING_MESSAGE)).create();

                                dialog = builder.create();
                                dialog.show();
                            }
                        }
                    }
                }
            }
        });
    }

    private void showAnalyzingDialog(){
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.v(LOG_TAG, "showAnalyzingDialog() enter");

                if( dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }

                Drawable icon = getResources().getDrawable(android.R.drawable.ic_dialog_alert);
                icon.setTint(getResources().getColor(R.color.colorPrimaryDark));

                // show dialog for analyzing
                builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.DIALOG_BREATHALYZER_ANALYZING_TITLE)
                        .setMessage(getString(R.string.DIALOG_BREATHALYZER_ANALYZING_MESSAGE))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v(LOG_TAG, "showAnalyzingDialog(): leave");
                            }
                        }).setIcon(icon).show();
            }
        });
    }

    private void showResultDialog(final float measuredResult){
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.v(LOG_TAG, "showResultDialog() enter");
                Log.d(LOG_TAG, "showResultDialog() result: " + measuredResult);

                if( dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }

                Drawable icon = getResources().getDrawable(android.R.drawable.ic_dialog_info);
                icon.setTint(getResources().getColor(R.color.colorPrimaryDark));

                // show dialog for results
                builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.DIALOG_BREATHALYZER_ANALYZED_TITLE).setMessage(getString(R.string.DIALOG_BREATHALYZER_ANALYZED_MESSAGE) + " " + measuredResult + " Promille")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v(LOG_TAG, "showResultDialog(): results leave");
                            }
                        }).setIcon(icon).show();
            }
        });
    }

    private class APIKeyVerificationAlert extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return urls[0];
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder apiApprovalAlert = new AlertDialog.Builder(mContext);
            apiApprovalAlert.setTitle("API Approval Failed");
            apiApprovalAlert.setMessage(result);
            apiApprovalAlert.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mAPI.disconnect();
                            setStatus(R.string.TEXT_DISCONNECTED);
                            dialog.cancel();
                        }
                    });

            apiApprovalAlert.create();
            apiApprovalAlert.show();
        }
    }


    // --------------- Handle BACtrackCallbacks

    private final BACtrackAPICallbacks mCallbacks = new BACtrackAPICallbacks() {
        @Override
        public void BACtrackAPIKeyDeclined(String errorMessage) {
            APIKeyVerificationAlert verify = new APIKeyVerificationAlert();
            verify.execute(errorMessage);
        }

        @Override
        public void BACtrackAPIKeyAuthorized() {

        }

        @Override
        public void BACtrackConnected(BACTrackDeviceType bacTrackDeviceType) {
            // show dialog for successful connection
            builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
            builder.setTitle(R.string.DIALOG_BREATHALYZER_CONNECTED_TITLE).setMessage(R.string.DIALOG_BREATHALYZER_CONNECTED__MESSAGE)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(LOG_TAG, "BACtrackConnected(): connection successful");
                        }
                    }).setIcon(android.R.drawable.ic_dialog_info).show();

            setStatus(R.string.TEXT_CONNECTED);
        }

        @Override
        public void BACtrackDidConnect(String s) {
            setStatus(R.string.TEXT_DISCOVERING_SERVICES);
        }

        @Override
        public void BACtrackDisconnected() {
            setStatus(R.string.TEXT_DISCONNECTED);
//            setBatteryStatus("");
//            setCurrentFirmware(null);


        }
        @Override
        public void BACtrackConnectionTimeout() {

        }

        @Override
        public void BACtrackFoundBreathalyzer(BACtrackAPI.b BACtrackDevice) {
            Log.v(LOG_TAG, "BACtrackFoundBreathalyzer(): enter");
            Log.d(LOG_TAG, "BACtrackFoundBreathalyzer(): found breathalyzer - '" + BACtrackDevice.toString() + "'");
            Log.v(LOG_TAG, "BACtrackFoundBreathalyzer(): leave");
        }

        @Override
        public void BACtrackCountdown(int currentCountdownCount) {
            Log.v(LOG_TAG, "BACtrackCountdown(): enter");

            showCountdownDialog(currentCountdownCount);

            Log.d(LOG_TAG, "BACtrackCountdown(): " + getString(R.string.TEXT_COUNTDOWN) + " " + currentCountdownCount);
            setStatus(getString(R.string.TEXT_COUNTDOWN) + " " + currentCountdownCount);
            Log.v(LOG_TAG, "BACtrackCountdown(): leave");
        }

        @Override
        public void BACtrackStart() {
            Log.v(LOG_TAG, "BACtrackStart(): enter");

            showBlowingDialog();

            Log.d(LOG_TAG, "BACtrackStart(): " + getString(R.string.TEXT_BLOW_NOW));
            setStatus(R.string.TEXT_BLOW_NOW);
            Log.v(LOG_TAG, "BACtrackStart(): leave");
        }

        @Override
        public void BACtrackBlow() {
            Log.v(LOG_TAG, "BACtrackBlow(): enter");

            Log.d(LOG_TAG, "BACtrackBlow(): " + getString(R.string.TEXT_KEEP_BLOWING));
            setStatus(R.string.TEXT_KEEP_BLOWING);
            Log.v(LOG_TAG, "BACtrackBlow(): leave");
        }

        @Override
        public void BACtrackAnalyzing() {
            Log.v(LOG_TAG, "BACtrackAnalyzing(): enter");

            // TODO: maybe show dialog with indefinite progress bar?
            showAnalyzingDialog();

            Log.d(LOG_TAG, "BACtrackAnalyzing(): " + getString(R.string.TEXT_ANALYZING));
            setStatus(R.string.TEXT_ANALYZING);
            Log.v(LOG_TAG, "BACtrackAnalyzing(): leave");
        }

        @Override
        public void BACtrackResults(float measuredBac) {
            Log.v(LOG_TAG, "BACtrackResults(): enter");

            // FIXME: convert the measured BAC from percent into permille
            // This is because the US uses another unit of measurement
            // (see: https://en.wikipedia.org/wiki/Blood_alcohol_content#Units_of_measurement)
            Log.d(LOG_TAG, "BACtrackResults(): " + getString(R.string.TEXT_FINISHED) + " " + measuredBac);
            setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac);

            showResultDialog(measuredBac);

            Log.v(LOG_TAG, "BACtrackResults(): leave");
        }

        @Override
        public void BACtrackFirmwareVersion(String version) {
            setStatus(getString(R.string.TEXT_FIRMWARE_VERSION) + " " + version);
        }

        @Override
        public void BACtrackSerial(String serialHex) {
            setStatus(getString(R.string.TEXT_SERIAL_NUMBER) + " " + serialHex);
        }

        @Override
        public void BACtrackUseCount(int useCount) {
            Log.d(LOG_TAG, "UseCount: " + useCount);
            // C6/C8 bug in hardware does not allow getting use count
            if (useCount == 4096)
            {
                setStatus("Cannot retrieve use count for C6/C8 devices");
            }
            else
            {
                setStatus(getString(R.string.TEXT_USE_COUNT) + " " + useCount);
            }
        }

        @Override
        public void BACtrackBatteryVoltage(float voltage) {

        }

        @Override
        public void BACtrackBatteryLevel(int level) {
//            setBatteryStatus(getString(R.string.TEXT_BATTERY_LEVEL) + " " + level);

        }

        @Override
        public void BACtrackError(int errorCode) {
            if (errorCode == Errors.ERROR_BLOW_ERROR)
                setStatus(R.string.TEXT_ERR_BLOW_ERROR);
        }
    };



}
