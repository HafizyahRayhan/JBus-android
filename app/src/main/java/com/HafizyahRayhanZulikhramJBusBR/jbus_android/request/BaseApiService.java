package com.HafizyahRayhanZulikhramJBusBR.jbus_android.request;

import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.Account;
import com.HafizyahRayhanZulikhramJBusBR.jbus_android.model.BaseResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);

    @POST("account/register")
    Call<BaseResponse<Account>> register (
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/login")
    Call<BaseResponse<Account>> login (
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/{id}/topUp")
    Call<BaseResponse<Double>> topUp (
            @Path("id") int id,
            @Query("amount") Double amount);

    @POST("account/{id}/registerRenter")
    Call<BaseResponse<Account>> registerRenter (
            @Path("id") int id,
            @Query("companyName") String companyName,
            @Query("address") String address,
            @Query("phoneNumber") String phoneNumber);
}
