package firstmob.firstbank.com.firstagent.contract;

import android.content.Context;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

public interface MainContract {

    interface Presenter {
        void doLogin(String attendant_Id);

        void ondestroy();
    }
    interface ILoginView {
        void onLoginResult(String result);
        void onLoginError(String error);
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