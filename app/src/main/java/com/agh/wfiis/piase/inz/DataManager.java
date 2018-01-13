package com.agh.wfiis.piase.inz;

/**
 * Created by piase on 2018-01-11.
 */

public interface DataManager {
    void onSuccess(boolean result, String message);
    void onException(String message);
}
