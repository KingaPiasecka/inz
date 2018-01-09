package com.agh.wfiis.piase.inz.models.pojo;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Date;

/**
 * Created by piase on 2017-12-18.
 */

public class Dust implements Comparable<Dust> {
    @SerializedName("id")
    String id;

    @SerializedName("dev_id")
    String devId;

    @SerializedName("datetime")
    Date dateTime;

    @SerializedName("lat")
    Double latitude;

    @SerializedName("lon")
    Double longitude;

    @SerializedName("alt")
    Double altitude;

    @SerializedName("speed")
    Double speed;

    @SerializedName("pm1")
    Double pm1;

    @SerializedName("pm25")
    Double pm25;

    @SerializedName("pm10")
    Double pm10;

    @SerializedName("temp")
    Double temperature;

    @SerializedName("r_hum")
    Double rHum;

    @SerializedName("p_atm")
    Double pAtm;

    @SerializedName("sat")
    Double sat;

    @SerializedName("HDOP")
    Double HDOP;

    @SerializedName("batt")
    Double batt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getPm1() {
        return pm1;
    }

    public void setPm1(Double pm1) {
        this.pm1 = pm1;
    }

    public Double getPm25() {
        return pm25;
    }

    public void setPm25(Double pm25) {
        this.pm25 = pm25;
    }

    public Double getPm10() {
        return pm10;
    }

    public void setPm10(Double pm10) {
        this.pm10 = pm10;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getrHum() {
        return rHum;
    }

    public void setrHum(Double rHum) {
        this.rHum = rHum;
    }

    public Double getpAtm() {
        return pAtm;
    }

    public void setpAtm(Double pAtm) {
        this.pAtm = pAtm;
    }

    public Double getSat() {
        return sat;
    }

    public void setSat(Double sat) {
        this.sat = sat;
    }

    public Double getHDOP() {
        return HDOP;
    }

    public void setHDOP(Double HDOP) {
        this.HDOP = HDOP;
    }

    public Double getBatt() {
        return batt;
    }

    public void setBatt(Double batt) {
        this.batt = batt;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public int compareTo(@NonNull Dust o) {
        return getDateTime().compareTo(o.getDateTime());
    }
}