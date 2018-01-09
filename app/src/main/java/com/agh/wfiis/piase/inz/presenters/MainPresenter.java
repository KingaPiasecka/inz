package com.agh.wfiis.piase.inz.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.agh.wfiis.piase.inz.models.DataManager;
import com.agh.wfiis.piase.inz.models.pojo.Dust;
import com.agh.wfiis.piase.inz.models.remote.APICallBack;
import com.agh.wfiis.piase.inz.presenters.chart.ChartInterface;
import com.agh.wfiis.piase.inz.presenters.chart.HumChartInterface;
import com.agh.wfiis.piase.inz.presenters.chart.PreChartInterface;
import com.agh.wfiis.piase.inz.presenters.chart.TempChartInterface;
import com.agh.wfiis.piase.inz.presenters.map.MapInterface;
import com.agh.wfiis.piase.inz.presenters.map.PM10MapInterface;
import com.agh.wfiis.piase.inz.presenters.map.PM1MapInterface;
import com.agh.wfiis.piase.inz.presenters.map.PM25MapInterface;
import com.agh.wfiis.piase.inz.utils.TimeAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.List;

/**
 * Created by piase on 2018-01-04.
 */

public class MainPresenter {

    private static int CHANGE_CHART_MEASUREMENT = 0;
    private static boolean CHANGE_CHART = false;
    private static int CHANGE_MAP_MEASUREMENT = 0;
    private static boolean CHANGE_MAP = false;
    private static boolean PAUSE = false;
    private static boolean END_OF_MEASUREMENTS = false;
    private static String DEVICE_ID = "11";

    public final Context context;
    public final GoogleMap mMap;
    public final LineChart chart;

    private TimeAxisValueFormatter iAxisValueFormatter = null;
    public ChartInterface chartInterface;
    public MapInterface mapInterface;

    private APICallBack apiCallBack;
    public DataManager dataManager;



    public MainPresenter(Context context, LineChart chart, GoogleMap mMap) {
        this.context = context;
        this.chart = chart;
        this.mMap = mMap;
        init();
    }

    private void init() {
        apiCallBack = new APICallBack();
        mapInterface = new PM10MapInterface(context);
        chartInterface = new TempChartInterface(context, chart);
        dataManager = new DataManager(this, context, apiCallBack);

    }

    public void sendAsyncRequest() {
        setChartPreferences();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        DEVICE_ID = settings.getString("deviceId", "");
        dataManager.invokeAsyncRequest();
    }


    public void sendCancelAsyncRequest() {
        dataManager.cancelAsyncRequest();
    }

    public void updateUI() {

        if (CHANGE_MAP) {
            changeMapData();
            CHANGE_MAP = false;
        }

        if (CHANGE_CHART) {
            changeChartData();
            CHANGE_CHART = false;
        }

        if (!PAUSE) {
            List<Dust> resultList = apiCallBack.getResultList();
            checkIfCorrectMeasurement(resultList);
            updateChart(resultList);
            updateMap(resultList);

        }

    }

    public void updateMap(List<Dust> dustList) {
        if (!dustList.isEmpty()) {
            for (Dust dust : dustList) {
                Log.i("updateMap:", dust.toString());
                MarkerOptions measurementMarker = mapInterface.getMeasurementMarker(dust);
                mMap.addMarker(measurementMarker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(measurementMarker.getPosition()));
            }
        }
    }

    public void changeMapData() {
        List<Dust> dusts = dataManager.getAllData();
        mMap.clear();
        switch (CHANGE_MAP_MEASUREMENT) {
            case 0:
                mapInterface = new PM10MapInterface(context);
                break;
            case 1:
                mapInterface = new PM25MapInterface(context);
                break;
            case 2:
                mapInterface = new PM1MapInterface(context);
                break;
        }
        if (!dusts.isEmpty()) {
            for (Dust dust : dusts) {
                MarkerOptions measurementMarker = mapInterface.getMeasurementMarker(dust);
                mMap.addMarker(measurementMarker);
            }
        }
    }


    public void updateChart(List<Dust> dustList) {
        if (!dustList.isEmpty()) {
            chartInterface.updateChart(dustList);
        }
    }

    private void setChartPreferences() {

        if (DataTimePresenter.isDateTimeChange()) {
            iAxisValueFormatter = new TimeAxisValueFormatter(new Date(DataTimePresenter.getStartingDateTime().getTimeInMillis()).getTime());

        } else {
            iAxisValueFormatter = new TimeAxisValueFormatter(new Date().getTime());

        }
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(iAxisValueFormatter);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1000f);
        chartInterface.setTimeAxisValueFormatter(iAxisValueFormatter);
        chart.getDescription().setTextSize(12f);

    }


    public void changeChartData() {

        List<Dust> dusts =  dataManager.getAllData();
        LineData chartData = chart.getData();

        if (chartData != null) {
            List<ILineDataSet> dataSets = chartData.getDataSets();

            for (ILineDataSet set : dataSets) {
                chartData.removeDataSet(set);
            }
        }

        switch (CHANGE_CHART_MEASUREMENT) {
            case 0:
                chartInterface = new TempChartInterface(context, chart);

                break;
            case 1:
                chartInterface = new HumChartInterface(context, chart);
                break;
            case 2:
                chartInterface = new PreChartInterface(context, chart);
                break;
        }
        chartInterface.setTimeAxisValueFormatter(iAxisValueFormatter);

        if (!dusts.isEmpty()) {
            chartInterface.updateChart(dusts);
        }
    }

    public void deleteData() {
        chart.clear();
        mMap.clear();
        dataManager.deleteDataCache();
        dataManager.deleteDataListCache();
    }

    public void setPAUSE(boolean PAUSE) {
        MainPresenter.PAUSE = PAUSE;
        apiCallBack.setUnPause(!PAUSE);
    }

    public static boolean isPAUSE() {
        return PAUSE;
    }

    public static String getDeviceId() {
        return DEVICE_ID;
    }

    public static void setDeviceId(String deviceId) {
        DEVICE_ID = deviceId;
    }

    public static void setChangeChartMeasurement(int changeChartMeasurement) {
        CHANGE_CHART_MEASUREMENT = changeChartMeasurement;
    }

    public static void setChangeChart(boolean changeChart) {
        CHANGE_CHART = changeChart;
    }

    public static void setChangeMapMeasurement(int changeMapMeasurement) {
        CHANGE_MAP_MEASUREMENT = changeMapMeasurement;
    }

    public static void setChangeMap(boolean changeMap) {
        CHANGE_MAP = changeMap;
    }

    public static boolean isEndOfMeasurements() {
        return END_OF_MEASUREMENTS;
    }

    public static void setEndOfMeasurements(boolean endOfMeasurements) {
        END_OF_MEASUREMENTS = endOfMeasurements;
    }

    public void checkIfCorrectMeasurement(List<Dust> resultList) {
        for (Dust dust : resultList) {
            if (dust.getLatitude() == 0 && dust.getLongitude() == 0) {
                resultList.remove(dust);
            }
        }
    }


}
