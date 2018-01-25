package com.agh.wfiis.piase.inz.models;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.agh.wfiis.piase.inz.DataManager;
import com.agh.wfiis.piase.inz.models.db.DatabaseAdapter;
import com.agh.wfiis.piase.inz.models.pojo.Dust;
import com.agh.wfiis.piase.inz.models.remote.APICallBack;
import com.agh.wfiis.piase.inz.presenters.DataTimePresenter;
import com.agh.wfiis.piase.inz.presenters.MainPresenter;
import com.agh.wfiis.piase.inz.utils.ToastMessage;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by piase on 2018-01-05.
 */

public class DataManagerImp implements DataManager{

    private MainPresenter mainPresenter;
    private TimerTask doAsynchronousTask;
    private APICallBack apiCallBack;
    private DatabaseAdapter databaseAdapter;
    private Context context;

    private boolean isProcessOver = true;

    public DataManagerImp(MainPresenter mainPresenter, Context context, APICallBack apiCallBack) {
        this.mainPresenter = mainPresenter;
        this.context = context;
        this.apiCallBack = apiCallBack;
        init();
    }

    public void init() {
        databaseAdapter = new DatabaseAdapter(context);
        databaseAdapter.open();
    }

    public void invokeAsyncRequest() {
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (apiCallBack.isExceptionStop()) {
                                cancelAsyncRequest();
                            }

                            if (isProcessOver) {
                                TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                                if (DataTimePresenter.isDateTimeChange()) {
                                    Date changedDateTime = DataTimePresenter.getStartingDateTime().getTime();
                                    apiCallBack.start(changedDateTime, MainPresenter.getDeviceId(), DataManagerImp.this);
                                    DataTimePresenter.setDateTimeChange(false);
                                } else if (!MainPresenter.isPAUSE()) {
                                    apiCallBack.start(new Date(), MainPresenter.getDeviceId(), DataManagerImp.this);
                                }

                                if (!MainPresenter.isPAUSE()) {
                                    List<Dust> resultList = apiCallBack.getResultList();
                                    databaseAdapter.insert(resultList);
                                }
                                mainPresenter.updateUI();
                            }

                        } catch (IllegalArgumentException e) {
                            ToastMessage.showToastMessage(e.getMessage());
                            cancelAsyncRequest();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000);
    }

    public void cancelAsyncRequest() {
        if (doAsynchronousTask != null) {
            Log.i("invoke", "cancelAsyncRequest");
            apiCallBack.deleteLatest();
            doAsynchronousTask.cancel();
        }
    }

    public List<Dust> getAllData() {
        return databaseAdapter.getAllElements();
    }

    public void deleteDataCache() {
        databaseAdapter.clearDatabase();
    }

    public void deleteDataListCache() {
        apiCallBack.clearDataCache();
    }

    public void setProcessOver(boolean processOver) {
        isProcessOver = processOver;
    }

    @Override
    public void onSuccess(boolean result, String message) {
        if (!result) {
            onException(message);
        }
    }

    @Override
    public void onException(String message) {
        ToastMessage.showToastMessage(message);
        cancelAsyncRequest();
    }
}
