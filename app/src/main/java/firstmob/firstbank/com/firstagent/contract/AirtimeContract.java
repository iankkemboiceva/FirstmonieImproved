package firstmob.firstbank.com.firstagent.contract;

import java.util.List;

import firstmob.firstbank.com.firstagent.model.GetAirtimeBillersData;

public interface AirtimeContract {
    public interface PresenterAirtimeFirst{
        void loadAirtimeBillers();
        void loadCachedAirtimeBillers();
        void ondestroy();
    }
    public interface IviewAirtimeFirst {
        void onResult(List<GetAirtimeBillersData> planetsList);
        void onProcessingError(String error);
        void logout();
        void showProgress();
        void hideProgress();
    }

    interface PresenterConfirmAirtime {
        void fetchServerfee(String flag,String extraparam);
        void ondestroy();
    }

    interface IViewConfirmAirtime {
        void setFee(String fee);
        void setviewvisibility();
        void onProcessingError(String error);
        void onBackNavigate();
        void setBalance(String blance);
        void logout();
        void showProgress();
        void hideProgress();

//        void openFinalConfirmAirtime(String agcmsn,String totfee,String tref);
    }


}
