package com.example.kisanseeva.Mandi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetroFitAPI {
    @GET
    Call<cropModel> getAllDetail(@Url String url);

//    Call<Crop>
}
