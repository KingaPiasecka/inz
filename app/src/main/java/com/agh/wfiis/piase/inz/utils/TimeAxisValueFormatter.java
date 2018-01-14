package com.agh.wfiis.piase.inz.utils;

import android.util.Log;

/*import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;*/

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Created by piase on 2017-12-26.
 */

public class TimeAxisValueFormatter implements IAxisValueFormatter {

    public long referenceTimestamp; // minimum timestamp in your data set
    private DateFormat dateFormat;
    private Date date;

    public TimeAxisValueFormatter(long referenceTimestamp) {
        this.referenceTimestamp = referenceTimestamp;
        this.dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        this.date = new Date();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        long convertedTimestamp = (long) value;
        long originalTimestamp = referenceTimestamp + convertedTimestamp;

        return getTime(originalTimestamp);
    }

    private String getTime(long timestamp) {
        try {
            date.setTime(timestamp);
            return dateFormat.format(date);
        } catch (Exception e) {
            Log.i("TimeAxisValueFormatter:", e.getMessage());
            return "HH:mm";
        }
    }

}
