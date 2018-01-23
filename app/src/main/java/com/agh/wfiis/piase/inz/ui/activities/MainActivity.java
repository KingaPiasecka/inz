package com.agh.wfiis.piase.inz.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.agh.wfiis.piase.inz.R;
import com.agh.wfiis.piase.inz.presenters.DataTimePresenter;
import com.agh.wfiis.piase.inz.presenters.MainPresenter;
import com.agh.wfiis.piase.inz.ui.fragments.DatePickerFragment;
import com.agh.wfiis.piase.inz.ui.fragments.TimePickerFragment;
import com.agh.wfiis.piase.inz.net.NetworkChangeReceiver;
import com.agh.wfiis.piase.inz.utils.ToastMessage;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    private MainPresenter mainPresenter;
    private GoogleMap mMap;
    private static LineChart chart;
    private Button startPauseButton;
    private Button stopButton;
    private DialogFragment newFragment;
    private NetworkChangeReceiver networkChangeReceiver;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private TimeZone timeZone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ToastMessage.setContext(getApplicationContext());

        Calendar calendar = Calendar.getInstance();
        if (timeZone == null) {
            timeZone = calendar.getTimeZone();
        }


        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();

        networkChangeReceiver  = new NetworkChangeReceiver(getApplicationContext());
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        chart =  findViewById(R.id.chart);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setText("");
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        /**
         * device id change
         */

        final Button optionsMenu = findViewById(R.id.optionsMenu);

        optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        /**
         * pick starting date and time
         */

        DataTimePresenter.setStartingDateTime(Calendar.getInstance());

        final Button pickerDateTime = findViewById(R.id.pickerDateTime);

        pickerDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTimePickerDialog(v);
                showDatePickerDialog(v);
            }
        });



        startPauseButton = findViewById(R.id.startPause);
        stopButton = findViewById(R.id.stop);

        startPauseButton.setTag(1);
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int status = (int) v.getTag();
                if (status == 1) {
                    //set buttons
                    startPauseButton.setText(R.string.pause);
                    stopButton.setEnabled(true);
                    startPauseButton.setTag(2);
                    //spinner.setEnabled(false);
                    pickerDateTime.setEnabled(false);
                    optionsMenu.setEnabled(false);

                    //clear data
                    mainPresenter.deleteData();

                    //start downloading data
                    mainPresenter.sendAsyncRequest();
                    MainPresenter.setEndOfMeasurements(false);


                } else if (status == 2) {
                    //set button
                    startPauseButton.setText(R.string.start);
                    v.setTag(3);

                    //pause downloading data
                    mainPresenter.setPAUSE(true);

                } else if (status == 3) {
                    //set button
                    startPauseButton.setText(R.string.pause);
                    v.setTag(2);

                    //restart downloading data
                    mainPresenter.setPAUSE(false);

                }
            }
        });

        stopButton.setEnabled(false);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Stop button:", "press");
                startPauseButton.setText(R.string.start);
                startPauseButton.setTag(1);
                stopButton.setEnabled(false);
                //spinner.setEnabled(true);
                pickerDateTime.setEnabled(true);
                optionsMenu.setEnabled(true);
                mainPresenter.sendCancelAsyncRequest();
                mainPresenter.dataManager.deleteDataListCache();
                mainPresenter.setPAUSE(false);
                MainPresenter.setEndOfMeasurements(true);
                TimeZone.setDefault(timeZone);
            }
        });


        /**
         * map changes
         */


        final Button pm10 = findViewById(R.id.pm10);
        final Button pm25 = findViewById(R.id.pm25);

        pm10.setEnabled(false);
        pm25.setEnabled(true);


        pm10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pm10.setEnabled(false);
                pm25.setEnabled(true);
                MainPresenter.setChangeMapMeasurement(0);
                MainPresenter.setChangeMap(true);
                if (MainPresenter.isEndOfMeasurements()) {
                    mainPresenter.changeMapData();
                }
            }
        });

        pm25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pm10.setEnabled(true);
                pm25.setEnabled(false);
                MainPresenter.setChangeMapMeasurement(1);
                MainPresenter.setChangeMap(true);
                if (MainPresenter.isEndOfMeasurements()) {
                    mainPresenter.changeMapData();
                }
            }
        });


        /**
         * chart changes
         */


        final Button temperature = findViewById(R.id.temperature);
        final Button humidity = findViewById(R.id.humidity);
        final Button pressure = findViewById(R.id.pressure);

        temperature.setEnabled(false);
        humidity.setEnabled(true);
        pressure.setEnabled(true);

        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temperature.setEnabled(false);
                humidity.setEnabled(true);
                pressure.setEnabled(true);
                MainPresenter.setChangeChartMeasurement(0);
                MainPresenter.setChangeChart(true);
                if (MainPresenter.isEndOfMeasurements()) {
                    mainPresenter.changeChartData();
                }
            }
        });

        humidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temperature.setEnabled(true);
                humidity.setEnabled(false);
                pressure.setEnabled(true);
                MainPresenter.setChangeChartMeasurement(1);
                MainPresenter.setChangeChart(true);
                if (MainPresenter.isEndOfMeasurements()) {
                    mainPresenter.changeChartData();
                }
            }
        });

        pressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temperature.setEnabled(true);
                humidity.setEnabled(true);
                pressure.setEnabled(false);
                MainPresenter.setChangeChartMeasurement(2);
                MainPresenter.setChangeChart(true);
                if (MainPresenter.isEndOfMeasurements()) {
                    mainPresenter.changeChartData();
                }
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng cracow = new LatLng(50.066749, 19.913299);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cracow));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mainPresenter = new MainPresenter(getApplicationContext(), chart, mMap);
    }

    public void showDatePickerDialog(View v) {
        newFragment = new DatePickerFragment();

        if (!isFinishing() && !isDestroyed()) {
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
    }

    public void showTimePickerDialog(View v) {
        newFragment = new TimePickerFragment();
        if (!isFinishing() && !isDestroyed()) {
            newFragment.show(getSupportFragmentManager(), "timePicker");
        }
    }

    @Override
    protected void onResume() {
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
        super.onDestroy();
    }
}
