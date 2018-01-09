package com.agh.wfiis.piase.inz.models.remote;

import com.agh.wfiis.piase.inz.models.pojo.Dust;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by piase on 2017-12-18.
 */

public interface APIInterface {

    @FormUrlEncoded
    @POST("/meteo/connection.php")
    Call<List<Dust>> getDusts(@Field("datetime") String datetime, @Field("dev_id") String dev_id);
}
