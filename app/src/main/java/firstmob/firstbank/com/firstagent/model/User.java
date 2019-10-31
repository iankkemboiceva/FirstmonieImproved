package firstmob.firstbank.com.firstagent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User{
    @SerializedName("STATUS")
    @Expose
    String status;
    @SerializedName("respdesc")
    @Expose
    String respdesc;

    public String getRespdesc() {
        return respdesc;
    }

    public String getStatus() {
        return status;
    }

}