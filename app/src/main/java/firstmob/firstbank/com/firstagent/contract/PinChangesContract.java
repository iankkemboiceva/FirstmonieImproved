package firstmob.firstbank.com.firstagent.contract;

public interface PinChangesContract {
    interface PresenterPinChange {
        void ServerLogRetroCall(String flag,String extraparam);
        void ServerRetroDevRegCall(String flag,String extraparam);
        void ondestroy();
    }

    interface IViewPinChange {
        void invokeRetroDevRegCall(String pubkey);
        void startSiginActivity();
        void ForceLogout();
        void onProcessingMessage(String message);
        void showProgress();
        void hideProgress();
    }
    interface IViewPinChangeActivity {
        void invokeRetroDevRegCall(String pubkey);
        void startSiginActivity();
        void ForceLogout();
        void onProcessingMessage(String message);
        void onBackpressd();
        void showProgress();
        void hideProgress();
    }
}
