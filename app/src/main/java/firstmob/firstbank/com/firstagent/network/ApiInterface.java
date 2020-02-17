package firstmob.firstbank.com.firstagent.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("{string}")
    Call<String> setGenericRequestRaw(@Path(value = "string", encoded = true) String string);

    @Headers("Content-Type: application/json")
    @POST("accountopen/app/bvn/openaccount.action")
    Call<String> bvnaccopen(@Body String params);

    @Headers("Content-Type: application/json")
    @POST("accountopen/app/core/openaccount.action")
    Call<String> nonbvnaccopen(@Body String params);

    @Headers("Content-Type: application/json")
    @POST("accountopen/app/bvn/validatebvn.action")
    Call<String> validatebvn(@Body String params);

}