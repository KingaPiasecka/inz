package com.agh.wfiis.piase.inz.presenters;

import java.util.Calendar;

/**
 * Created by piase on 2018-01-04.
 */

public class DataTimePresenter {
    private static Calendar STARTING_DATE_TIME;
    private static boolean DATE_TIME_CHANGE;

    public static Calendar getStartingDateTime() {
        return STARTING_DATE_TIME;
    }

    public static void setStartingDateTime(Calendar startingDateTime) {
        STARTING_DATE_TIME = startingDateTime;
    }

    public static boolean isDateTimeChange() {
        return DATE_TIME_CHANGE;
    }

    public static void setDateTimeChange(boolean dateTimeChange) {
        DATE_TIME_CHANGE = dateTimeChange;
    }

    public static void setTime(int hourOfDay, int minute) {
        STARTING_DATE_TIME.set(Calendar.HOUR_OF_DAY, hourOfDay);
        STARTING_DATE_TIME.set(Calendar.MINUTE, minute);
        DATE_TIME_CHANGE = true;
    }

    public static void  setDate(int year, int month, int day) {
        STARTING_DATE_TIME.set(Calendar.YEAR, year);
        STARTING_DATE_TIME.set(Calendar.MONTH, month);
        STARTING_DATE_TIME.set(Calendar.DAY_OF_MONTH, day);
    }

}
