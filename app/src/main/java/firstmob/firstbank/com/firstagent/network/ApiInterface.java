package firstmob.firstbank.com.firstagent.network;

import android.graphics.Movie;

import firstmob.firstbank.com.firstagent.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("{string}")
    Call<String> setGenericRequestRaw(@Path(value = "string", encoded = true) String string);

}