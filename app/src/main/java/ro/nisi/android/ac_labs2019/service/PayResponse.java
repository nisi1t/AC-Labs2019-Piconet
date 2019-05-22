package ro.nisi.android.ac_labs2019.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayResponse {
    @SerializedName("success")
    @Expose
    public final boolean success;

    @SerializedName("message_id")
    @Expose
    public final boolean message_id;

    public PayResponse(boolean success, boolean message_id) {
        this.success = success;
        this.message_id = message_id;
    }
}