package firstmob.firstbank.com.firstagent.contract;

import org.json.JSONArray;

public interface MyPerfActivityContract {
    interface PresenterPerfAct {
        void ServerPullDataCall(String extraparam);
        void ondestroy();
    }

    interface IViewPerfAct {
        void setUpActivity(JSONArray summdata,JSONArray comperf);
        void startSiginActivity();
        void ForceLogout();
        void onProcessingMessage(String message);
        void showProgress();
        void hideProgress();
    }

}
