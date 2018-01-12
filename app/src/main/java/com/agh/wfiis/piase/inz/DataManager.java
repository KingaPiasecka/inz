package com.agh.wfiis.piase.inz;

/**
 * Created by piase on 2018-01-11.
 */

public interface DataManager {
    public void onSuccess(boolean result, String message);
    public void onException(String message);
}
