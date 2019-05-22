package ro.nisi.android.ac_labs2019.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EasyPayService {

    @FormUrlEncoded
    @POST("pay")
    // {"success": true, "message_id": true}
    Call<PayResponse> pay(@Field("recipient") String recipient, @Field("prefix") String prefix, @Field("license_plate") String licensePlate);

    // {"name":"EasyPay API","version":"v0.1","status":true}
    @GET("/")
    Call<StatusResponse> status();
}
