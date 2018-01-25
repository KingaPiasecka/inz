package com.agh.wfiis.piase.inz.models.remote;

import android.content.Context;
import android.util.Log;

import com.agh.wfiis.piase.inz.DataManager;
import com.agh.wfiis.piase.inz.models.pojo.Dust;
import com.agh.wfiis.piase.inz.restapi.APIClient;
import com.agh.wfiis.piase.inz.utils.ToastMessage;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by piase on 2017-12-18.
 */

public class APICallBack implements Callback<List<Dust>> {

    private Call<List<Dust>> call;
    private List<Dust> resultList;
    private boolean unPause;
    private DateFormat dateFormat;
    private Date latest;
    private Context context;
    private boolean exceptionStop = false;
    private DataManager dataManager;


    public APICallBack(Context context) {
        this.resultList = new ArrayList<>();
        this.unPause = false;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        dateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        this.context = context;
    }

    public void start(Date startingDateTime, String devId, DataManager dataManager) {
        this.dataManager = dataManager;
        APIInterface apiInterface = APIClient.getRetrofit(context).create(APIInterface.class);
        exceptionStop = false;
        if (latest == null || unPause) {
            latest = startingDateTime;
            call = apiInterface.getDusts(dateFormat.format(latest.getTime()), devId);
            Log.i("Start from:" + dateFormat.format(startingDateTime), "devId:" + devId);
            unPause = false;
        } else {
            String date = dateFormat.format(latest.getTime());
            call = apiInterface.getDusts(date, devId);
            Log.i("Get data from:", date);
        }
        try {
            call.enqueue(this);
        } catch (Exception e) {
            Log.e("APICallBack.class:", "" + e);
        }
    }

    private Dust getTheNewestOne() {
        Collections.sort(resultList);
        return resultList.get(resultList.size() - 1);
    }

    @Override
    public void onResponse(Call<List<Dust>> call, Response<List<Dust>> response) {
        if (response.isSuccessful()) {
            resultList = response.body();
            if (!resultList.isEmpty()) {
                latest = getTheNewestOne().getDateTime();
                Headers headers = response.headers();
                Log.i("headers: ", "" + headers.toString());
            }
        } else {
            Log.e("Error Code", String.valueOf(response.code()));
            if (response.code() == 401) {
                dataManager.onSuccess(false, "User not authenticated");
            } else if (response.code() == 500) {
                dataManager.onSuccess(false, "Internal Server Error");
            } else {
                dataManager.onSuccess(false, "HTTP status code:" + response.code());
            }
            try {
                Log.e("Error Body", response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<List<Dust>> call, Throwable throwable) {
        resultList = new ArrayList<>();
        Log.i("onFailure", "" + throwable.getMessage());

        if (throwable instanceof UnknownHostException) {
            exceptionStop = true;
            ToastMessage.showToastMessage(throwable.getMessage());
        }
    }

    public List<Dust> getResultList() {
        return resultList;
    }

    public void clearDataCache() {
        resultList = new ArrayList<>();
    }

    public void setUnPause(boolean unPause) {
        this.unPause = unPause;
    }

    public boolean isExceptionStop() {
        return exceptionStop;
    }

    public void deleteLatest() {
        this.latest = null;
    }
}
