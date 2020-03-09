package firstmob.firstbank.com.firstagent.model;

/**
 * Created by deeru on 18-10-2016.
 */

import com.google.gson.annotations.SerializedName;


public class GetStores implements  Comparable<GetStores>  {


    @SerializedName("storeid")
    private String storeid;

    @SerializedName("storename")
    private String storename;

    public GetStores(String storeid, String storename) {

        this.storeid = storeid;

        this.storename = storename;

    }


    public String getstoreid() {
        return storeid;
    }

    public void setstoreid(String accnum) {
        this.storeid = accnum;
    }


    public String getstorename() {
        return storename;
    }

    public void setstorename(String accnum) {
        this.storename = accnum;
    }

    @Override
    public int compareTo(GetStores o) {
        return this.storename.compareTo(o.storename); // dog name sort in ascending order
        //return o.getName().compareTo(this.name); use this line for dog name sort in descending order
    }

    @Override
    public String toString() {
        return this.storename;
    }


}