package firstmob.firstbank.com.firstagent.contract;

import java.util.List;

import firstmob.firstbank.com.firstagent.model.BenList;
import firstmob.firstbank.com.firstagent.model.GetBillPayData;
import firstmob.firstbank.com.firstagent.model.GetBillersData;
import firstmob.firstbank.com.firstagent.model.GetServicesData;

public interface GetBillersContract {
    interface Presenterloadbillers{
        void loadbiller();
        void loadcachedbiilersData();
        void ondestroy();
    }

    interface IViewbillers {
        void onResult(List<GetServicesData> BillerList);
        void onProcessingError(String error);
        void logoutuser();
        void showProgress();
        void hideProgress();
    }
interface Presenterloadbillerspc {
    void loadbiller(String extraparams);
    void loadcachedbiilersData(String extraparams);
    void ondestroy();
}
    interface IViewbillersSpec {
        void onResult(List<GetBillersData> BillerList);
        void onProcessingError(String error);
        void logoutuser();
        void showProgress();
        void hideProgress();
    }
    interface IViewbillPaymentsAct {
        void onResult(List<GetBillPayData> BillerList);
        void onProcessingError(String error);
        void navigatetoNextpage();
        void logoutuser();
        void showProgress();
        void hideProgress();
    }
    interface IViewbillStateCollect {
        void onResult(List<BenList> BillerList);
        void onProcessingError(String error);
        void logoutuser();
        void showProgress();
        void hideProgress();
    }
    interface PresenterLoadMarket{
        void loadMarketPlaces(String extrparams);
        void ondestroy();
    }
    interface IViewbillConfirmCabletv {
        void onProcessingError(String error);
        void setBalance(String balance);
        void setFee(String fee);
        void checkfee(Boolean chekfee);
        void setFullNames(String fullnames);
        void  onBackpressed();
        void logoutuser();
        void showProgress();
        void hideProgress();
    }
    interface PresenterConfirmCabletv{
        void RequestServerfee(String extrparams,String serviceidp);
        void RequestServervalidate(String extrparams);
        void ondestroy();
    }
}
