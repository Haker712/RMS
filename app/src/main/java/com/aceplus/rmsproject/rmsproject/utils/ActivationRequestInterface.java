package com.aceplus.rmsproject.rmsproject.utils;

import com.aceplus.rmsproject.rmsproject.object.ActivateKey;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Dell on 9/14/2017.
 */

public interface ActivationRequestInterface {

    @FormUrlEncoded
    @POST("/api/activate")
    Call<ActivateKey> getActivation(@Field("param_data") String paramData);

}
