package firstmob.firstbank.com.firstagent.contract;

public interface SignInContract {

    interface Presenter {
        void Login(String agpin);

        void setAppVersion();

        void ondestroy();
    }
    interface ILoginView {
        void onLoginResult();

        void onLoginError(String error);
        void setTextApp(String version);

        void showToast(String text);

        void showProgress();

        void hideProgress();

    }


}