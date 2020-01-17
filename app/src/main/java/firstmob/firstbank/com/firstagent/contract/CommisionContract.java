package firstmob.firstbank.com.firstagent.contract;

import java.util.List;

import firstmob.firstbank.com.firstagent.model.GetAirtimeBillersData;
import firstmob.firstbank.com.firstagent.model.MinistatData;

public interface CommisionContract {
    interface Presenter{
        void requestCall(String flag,String extraparam);
        void ondestroy();
    }
    interface IViewCommission {
        void onResult(String flag,String response);
        void onProcessingError(String error);
        void showProgress();
        void hideProgress();
    }
    interface PresenterMinista{
        void requestCallMinistat(String flag,String extraparam);
        void requestCallGetBalnce(String flag,String extraparam);
        void ondestroy();
    }
    interface IViewMinistatement {
        void onProcessingError(String error);
        void NavigateToSognIn();
        void PopulateRecyclerView(List<MinistatData> planetsList);
        void showProgress();
        void setMinistatementStartim();
        void ForceLogout();
        void setBalance(String balance);
        void hideProgress();
    }
}
