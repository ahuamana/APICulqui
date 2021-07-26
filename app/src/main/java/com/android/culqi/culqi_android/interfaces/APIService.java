package com.android.culqi.culqi_android.interfaces;

import com.android.culqi.culqi_android.CardModel.CardModel;
import com.android.culqi.culqi_android.CardModel.CardSender;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {


    @Headers({
            "Content-type: application/json",
            "Authorization: Bearer pk_test_c06043ec9b052aa6"
    })
    @POST("tokens")
    Call<JsonObject> getToken(
            @Body CardSender body
            );



}
