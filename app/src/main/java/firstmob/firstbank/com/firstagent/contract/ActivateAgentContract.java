package firstmob.firstbank.com.firstagent.contract;

public interface ActivateAgentContract {

    interface Presenter {
        void DevReg(String agpin,String otp);
        void ResendOTP(String agid);
        void ondestroy();
    }
    interface ILoginView {
        void onLoginResult();

        void onLoginError(String error);

        void showToast(String text);

        void showProgress();

        void hideProgress();

    }

        interface GetResendataIntractor {
            interface OnResendFinishedListener {
                void onResendFinished(String response);

                void onFailure(Throwable t);
            }

            void getResendOTPResults(OnResendFinishedListener OnResendFinishedListener, String urlparams);
        }



}