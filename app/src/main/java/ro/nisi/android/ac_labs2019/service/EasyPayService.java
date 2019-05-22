package ro.nisi.android.ac_labs2019.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EasyPayService {

    @FormUrlEncoded
    @POST("pay")
    Call<PayResponse> pay(@Field("recipient") String recipient, @Field("prefix") String prefix, @Field("license_plate") String licensePlate);
}
