package app.camalyzer.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 7/3/2015.
 */
public class RestError {

    @SerializedName("status")
    private String mErrStatus;

    public RestError(String mErrStatus) {
        this.mErrStatus = mErrStatus;
    }

}
