package com.hpsaturn.ourhabitat.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.hpsaturn.ourhabitat.Config;
import com.hpsaturn.ourhabitat.MainActivity;
import com.hpsaturn.ourhabitat.R;
import com.hpsaturn.ourhabitat.tools.Tools;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by hasus on 3/31/15.
 */
public class AddDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    public static final String TAG = AddDialogFragment.class.getSimpleName();
    private static final boolean DEBUG = Config.DEBUG;


    private Spinner _sp_contacts;
    private EditText _et_summary;
    private EditText _et_city;
    private EditText _et_address;
    private EditText _et_time;
    private EditText _et_date;
    private ImageButton _bt_add;
    private Spinner _sp_priority;
    private Button _bt_more;
    private RelativeLayout _rl_more;

    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private int hourOfDay;
    private int minute;
    private long selectVisitTime;
    private int priority = 0;

    private String summary;
    private String city;
    private String address;

    private boolean lessInfo = true;

    final Calendar c = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_task,container,false);

        getDialog().setTitle(R.string.add_dialog_title);
        _et_summary = (EditText) v.findViewById(R.id.et_add_dialog_summary);
        _et_city = (EditText) v.findViewById(R.id.et_add_dialog_city);
        _et_address = (EditText) v.findViewById(R.id.et_add_dialog_address);

//        _sp_contacts = (Spinner) v.findViewById(R.id.sp_add_dialog_contacts);
//        _sp_contacts.setOnItemSelectedListener(this);

        _sp_priority = (Spinner) v.findViewById(R.id.sp_add_dialog_priority);

        _et_time = (EditText) v.findViewById(R.id.et_add_dialog_time);
        _et_time.setOnClickListener(onTimeClickListener);
        _et_time.setFocusable(false);

        _et_date = (EditText) v.findViewById(R.id.et_add_dialog_date);
        _et_date.setOnClickListener(onDateClickListener);
        _et_date.setFocusable(false);

        _bt_add = (ImageButton) v.findViewById(R.id.bt_add_dialog);
        _bt_add.setOnClickListener(onAddTaskClickListener);

        _bt_more = (Button) v.findViewById(R.id.bt_add_dialog_more);
        _bt_more.setOnClickListener(onMoreClickListener);

        _rl_more = (RelativeLayout) v.findViewById(R.id.rl_add_dialog_more);

        initPickers();
        initSpinner();

//        getContactList();
//        getGeoCodeInverse();
//        updateCurrentAddress();

        return v;

    }

    private void addTask() {

        if(isValidFields()){


        }

    }

    private boolean isValidFields() {

        loadFields();

//        if(summary.length()==0){
//            Tools.showToast(getActivity(), R.string.add_dialog_summary_error);
//            return false;
//        }
////        else if (selectContact==null){
////            UITools.showToast(getActivity(),R.string.add_dialog_contact_error);
////            return false;
////        }
//        else if (address.length()==0){
//            UITools.showToast(getActivity(),R.string.add_dialog_address_error);
//            return false;
//        }
//        else if (!lessInfo&&city.length()==0){
//            UITools.showToast(getActivity(),R.string.add_dialog_city_error);
//            return false;
//        }
//        else if (!lessInfo&&(int)selectVisitTime/1000L<=(int) (c.getTimeInMillis())/1000L){
//            UITools.showToast(getActivity(),R.string.add_dialog_time_error);
//            return false;
//        }

        return true;
    }

    private void loadFields() {

        if(lessInfo){
            summary = _et_summary.getText().toString();
            address = _et_address.getText().toString();
            city = _et_city.getText().toString();
        }else {
            summary = _et_summary.getText().toString();
            city = _et_city.getText().toString();
            address = _et_address.getText().toString();
            selectVisitTime = Tools.componentTimeToTimestamp(year, monthOfYear, dayOfMonth, hourOfDay, minute);
            priority = _sp_priority.getSelectedItemPosition();
        }

    }

    public void showTimePickerDialog(View v) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getFragmentManager(), TimePickerFragment.TAG);
    }

    public void showDatePickerDialog(View v){
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), DatePickerFragment.TAG);
    }

    private View.OnClickListener onTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTimePickerDialog(v);
        }
    };

    private View.OnClickListener onDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDatePickerDialog(v);
        }
    };


    private View.OnClickListener onAddTaskClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            addTask();
        }
    };

    private View.OnClickListener onMoreClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            lessInfo = false;
            if (_bt_more.getText().equals(getResources().getString(R.string.add_dialog_more))) {
                _bt_more.setText(getResources().getString(R.string.add_dialog_less));
                _rl_more.setVisibility(View.VISIBLE);
            } else {
                lessInfo = true;
                _bt_more.setText(getResources().getString(R.string.add_dialog_more));
                _rl_more.setVisibility(View.GONE);
            }

        }

    };


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if(DEBUG)Log.d(TAG, "onItemSelected: "+position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initPickers(){
        updateTimeField(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        updateDateField(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    private void initSpinner(){
        String[] sp_priority = getActivity().getResources().getStringArray(R.array.priority_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,sp_priority);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _sp_priority.setAdapter(adapter);
    }

    public void updateTimeField(int hourOfDay, int minute) {
        this.hourOfDay=hourOfDay;
        this.minute=minute;
        _et_time.setText("" + hourOfDay + ":" + minute);
    }

    public void updateDateField(int year, int monthOfYear, int dayOfMonth) {
        this.year=year;
        this.monthOfYear=monthOfYear;
        this.dayOfMonth=dayOfMonth;
        _et_date.setText("" + year + "." + (monthOfYear+1) + "." + dayOfMonth);
    }

    private MainActivity getMain() {
        return ((MainActivity)getActivity());
    }

    public void updateCurrentAddress (){

//       Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Location lastLocation = getMain().getLastLocation();
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();

            //Once you get the coordinates, you can retrieve the city name using the Geocoder class:
            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
                if (addresses != null&&addresses.size()>0) {
                    String adressLine=addresses.get(0).getAddressLine(0);
                    String locality=addresses.get(0).getLocality();
                    if(locality!=null&&locality.length()>0) _et_city.setText(locality);
                    else _et_city.setText(R.string.default_city);
                    _et_address.setText(adressLine);
                    if(DEBUG) Log.i(TAG, "GeoCoder found: " + adressLine + " " + locality);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else
            if(DEBUG) Log.d(TAG, "lastLocation is NULL!");

    }

}
