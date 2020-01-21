package firstmob.firstbank.com.firstagent.contract;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import firstmob.firstbank.com.firstagent.model.GetAirtimeBillersData;

public interface MainContract {

    interface Presenter {
        void doLogin(String attendant_Id);
        void ondestroy();
    }
    interface IView {
        void onfetchResult();
        void onError(String error);
        void showToast(String text);
        void showProgress();
        void hideProgress();
    }


        interface GetDataIntractor {
        interface OnFinishedListener {
            void onFinished(String response);
            void onFailure(Throwable t);
        }
        void getResults(OnFinishedListener onFinishedListener,String urlparams);
    }


}