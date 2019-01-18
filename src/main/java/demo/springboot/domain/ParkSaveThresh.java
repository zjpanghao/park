package demo.springboot.domain;

import com.google.gson.annotations.SerializedName;

public class ParkSaveThresh {
    @SerializedName("error_code")
    private int errorCode;

    @SerializedName("low")
    private int low;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }
}
