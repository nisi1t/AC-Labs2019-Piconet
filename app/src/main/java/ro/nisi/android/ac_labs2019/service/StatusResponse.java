package ro.nisi.android.ac_labs2019.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusResponse {
    @SerializedName("name")
    @Expose
    public final String name;

    @SerializedName("version")
    @Expose
    public final String version;

    @SerializedName("status")
    @Expose
    public final boolean status;

    public StatusResponse(String name, String version, boolean status) {
        this.name = name;
        this.version = version;
        this.status = status;
    }
}