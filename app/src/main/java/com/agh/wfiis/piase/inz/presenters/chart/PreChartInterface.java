package com.agh.wfiis.piase.inz.presenters.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.agh.wfiis.piase.inz.models.pojo.Dust;
import com.agh.wfiis.piase.inz.utils.TimeAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

/**
 * Created by piase on 2017-12-30.
 */

public class PreChartInterface implements ChartInterface{

    private final Context context;
    private LineChart chart;
    private TimeAxisValueFormatter iAxisValueFormatter;

    public PreChartInterface(Context context, LineChart lineChart) {
        this.context = context;
        this.chart = lineChart;
    }

    @Override
    public void updateChart(List<Dust> dustList) {
        if (!dustList.isEmpty()) {

            LineData chartData = chart.getData();
            LineDataSet dataSetByIndex;

            if (chartData == null) {
                chartData = new LineData();
                chart.setData(chartData);

                dataSetByIndex = (LineDataSet) chartData.getDataSetByIndex(0);

                if (dataSetByIndex == null) {
                    dataSetByIndex = new LineDataSet(null, "Data");
                    chartData.addDataSet(dataSetByIndex);
                }

            } else {

                dataSetByIndex = (LineDataSet) chartData.getDataSetByIndex(0);

                if (dataSetByIndex == null) {
                    dataSetByIndex = new LineDataSet(null, "Data");
                    chartData.addDataSet(dataSetByIndex);
                }

            }
            dataSetByIndex.setDrawValues(false);
            dataSetByIndex.setDrawCircleHole(false);
            dataSetByIndex.setDrawCircles(false);
            dataSetByIndex.setLineWidth(2f);
            dataSetByIndex.setColors(Color.parseColor("#78e04f"));

            for (Dust dust : dustList) {
                long l = dust.getDateTime().getTime() - iAxisValueFormatter.referenceTimestamp;
                Log.i("updateChart: ", dust.toString());
                chartData.addEntry(new Entry(l, dust.getpAtm().floatValue()),0);
                chart.getDescription().setText("Pressure: " + dust.getpAtm() + " (hPa)");
            }

            chartData.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();

        }
    }

    @Override
    public void setTimeAxisValueFormatter(TimeAxisValueFormatter iAxisValueFormatter) {
        this.iAxisValueFormatter = iAxisValueFormatter;
    }
}
