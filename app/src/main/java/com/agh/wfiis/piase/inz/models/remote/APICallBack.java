package com.agh.wfiis.piase.inz.models.remote;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.agh.wfiis.piase.inz.models.DataManager;
import com.agh.wfiis.piase.inz.models.pojo.Dust;
import com.agh.wfiis.piase.inz.restapi.APIClient;

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


    public APICallBack(Context context) {
        this.resultList = new ArrayList<>();
        this.unPause = false;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        this.context = context;
    }

    public void start(Date startingDateTime, String devId) {

        APIInterface apiInterface = APIClient.getRetrofit(context).create(APIInterface.class);
        exceptionStop = false;
        if (resultList.isEmpty() || unPause) {
            call = apiInterface.getDusts(dateFormat.format(startingDateTime), devId);
            Log.i("Start from:" + dateFormat.format(startingDateTime), "devId:" + devId);
            unPause = false;
        } else  if (!resultList.isEmpty()){
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
        resultList = response.body();
        if (!resultList.isEmpty()) {
            latest = getTheNewestOne();
            Log.i("onResponse: ", "" + resultList.get(0).toString());
        }
        Headers headers = response.headers();
        Log.i("headers: ", "" + headers.toString());

    }

    @Override
    public void onFailure(Call<List<Dust>> call, Throwable throwable) {
        resultList = new ArrayList<>();
        Log.i("onFailure", "" + throwable.getMessage());

        if (throwable instanceof UnknownHostException) {
            exceptionStop = true;
            Toast toast = Toast.makeText(this.context, throwable.getMessage(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
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
