package firstmob.firstbank.com.firstagent.network;

import android.util.Log;

import firstmob.firstbank.com.firstagent.contract.MainContract;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bpn on 12/7/17.
 */

public class FetchServerResponse implements MainContract.GetDataIntractor {

    @Override
    public void getResults(final OnFinishedListener onFinishedListener, String urlparams) {


        /** Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<String> call = service.setGenericRequestRaw(urlparams);

        /**Log the URL called*/
        Log.i("URL Called", urlparams);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    onFinishedListener.onFinished(response.body());
                } catch (Exception e) {
                    onFinishedListener.onFailure(e);
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });

    }

}
