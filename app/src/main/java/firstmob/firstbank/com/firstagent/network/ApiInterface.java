package firstmob.firstbank.com.firstagent.network;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("{string}")
    Call<String> setGenericRequestRaw(@Path(value = "string", encoded = true) String string);

}