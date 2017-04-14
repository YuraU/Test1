package com.yura.test1.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiService {

    @GET("/")
    Call<ResponseBody> getDate();
}
