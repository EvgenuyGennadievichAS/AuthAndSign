package com.rdv.slcard.API;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {


    @FormUrlEncoded
    @POST("token")
    Call<ResultRegister> registerBack(@FieldMap Map<String,String> register);

    @FormUrlEncoded
    @POST("token")
    Call<ResultLogin> inSignBack(@FieldMap Map<String,String> inSign);

    @FormUrlEncoded
    @POST("token")
    Call<ChangePassword> changePasswordBack(@FieldMap Map<String,String> changePassword);

    @GET("token")
    Call<ArrayList<Category>> categoryPartner();

    @GET("token")
    Call<Partner> partnerId(@Path("id") Integer id);

    @GET("token")
    Call<ArrayList<Stocks>> getStocks();

    @GET("/token")
    Call<ArrayList<Map<String,String>>> getCity();


}
