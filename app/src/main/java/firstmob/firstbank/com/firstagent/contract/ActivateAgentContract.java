package firstmob.firstbank.com.firstagent.contract;

public interface ActivateAgentContract {

    interface Presenter {
        void DevReg(String agpin,String otp);

        void ondestroy();
    }
    interface ILoginView {
        void onLoginResult(String result);
        void onLoginError(String error);
        void showToast(String text);
        void showProgress();
        void hideProgress();
    }



}