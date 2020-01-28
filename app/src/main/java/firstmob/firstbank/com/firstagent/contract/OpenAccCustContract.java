package firstmob.firstbank.com.firstagent.contract;

public interface OpenAccCustContract {

    interface Presenter {
        void GenCustOTP(String acno);

    }
    interface ILoginView {
        void onfetchResult();
        void onError(String error);
        void showToast(String text);
        void showProgress();
        void hideProgress();


    }

    interface GetDataIntractor {
        interface OnFinishedListener {
            void onFinished(String response);
            void onFailure(Throwable t);
        }
        void getResults(OpenAccCustContract.GetDataIntractor.OnFinishedListener onFinishedListener, String urlparams);
    }



}