package app.camalyzer.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 7/3/2015.
 */
public class RestError {

    @SerializedName("url")
    public String mErrStatus;

    @SerializedName("token")
    private String mToken;

    @SerializedName("status")
    private String mStatus;

    @SerializedName("name")
    private String mName;

    public RestError(String mErrStatus, String token, String mStatus, String mName) {
        this.mErrStatus = mErrStatus;
        mToken = token;
        this.mStatus = mStatus;
        this.mName = mName;

    }

    public String getToken() {
        return mToken;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getName() {
        return mName;
    }

}
