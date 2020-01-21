package firstmob.firstbank.com.firstagent.contract;

public interface WithdrawalsContract {
    interface PresenterGen{
        void requestCallGetotp(String flag,String extraparam);
        void requestCallNameInquiry(String flag,String extraparam);
        void ondestroy();
    }
    interface IViewWithdrawalFirst {
        void AccountName(String accountName);
        void setAccountName();
        void requestOtp();
        void setViewVisibility();
        void setReference(String reference);
        void onProcessingError(String error);
        void showProgress();
        void hideProgress();
    }

    interface ConfirmWithdralPresenter {
        void loaddata(String extraparam);
        void ondestroy();
    }
    interface IViewConfirmWithdrawal {
        void onError(String error);
        void setFee(String fee);
        void setBalance(String Balance);
        void launchWithdrawFrag();
        void setViewVisibillity();
        void setForcedLogout();
        void setLogout();
        void showProgress();
        void hideProgress();
    }
}
