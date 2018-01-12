package com.agh.wfiis.piase.inz.models.remote;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;

import com.agh.wfiis.piase.inz.DataManager;
import com.agh.wfiis.piase.inz.models.pojo.Dust;
import com.agh.wfiis.piase.inz.restapi.APIClient;
import com.agh.wfiis.piase.inz.utils.ToastMessage;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    private Dust latest;
    private Context context;
    private boolean exceptionStop = false;
    private DataManager dataManager;


    public APICallBack(Context context) {
        this.resultList = new ArrayList<>();
        this.unPause = false;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        this.context = context;
    }

    public void start(Date startingDateTime, String devId, DataManager dataManager) {
        this.dataManager = dataManager;
        APIInterface apiInterface = APIClient.getRetrofit(context).create(APIInterface.class);
        exceptionStop = false;
        if (resultList.isEmpty() || unPause) {
            call = apiInterface.getDusts(dateFormat.format(startingDateTime), devId);
            Log.i("Start from:" + dateFormat.format(startingDateTime), "devId:" + devId);
            unPause = false;
        } else if (!resultList.isEmpty()) {
            Date dateTime = latest.getDateTime();
            dateTime.setTime(dateTime.getTime() + 1000);
            String date = dateFormat.format(dateTime);
            call = apiInterface.getDusts(date, devId);
            Log.i("get data from:", date);
        }
        try {
            call.enqueue(this);
        } catch (Exception e) {
            Log.e("APICallBack.class:", e.getMessage());
        }
    }

    private Dust getTheNewestOne() {
        Collections.sort(resultList);
        Log.i("newest:", "" + resultList.get(0).toString());
        return resultList.get(resultList.size() - 1);
    }

    @Override
    public void onResponse(Call<List<Dust>> call, Response<List<Dust>> response) {
        if (response.isSuccessful()) {
            resultList = response.body();
            if (!resultList.isEmpty()) {
                latest = getTheNewestOne();
                Headers headers = response.headers();
                Log.i("headers: ", "" + headers.toString());
            }
        } else {

            if (response.code() == 401) {
                dataManager.onSuccess(false, "User not authenticated");
                //throw new UserNotAuthenticatedException("User not authenticated");
            }

            Log.e("statusBody: ", "" +String.valueOf(response.code()));
            Log.e("errorBody: ", "" + response.errorBody().toString());
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
        Log.i("APICallBack: result", "" + resultList.size());
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
}
