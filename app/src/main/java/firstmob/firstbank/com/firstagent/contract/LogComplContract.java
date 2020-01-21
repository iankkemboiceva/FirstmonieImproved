package firstmob.firstbank.com.firstagent.contract;

public interface LogComplContract {

    interface Presenter {
        void LogComp(String acno,String amount,String datetime,String desc);



        void ondestroy();
    }
    interface ILoginView {


        void showToast(String text);
        void onlogcompresult();



        void showProgress();

        void hideProgress();


    }


}