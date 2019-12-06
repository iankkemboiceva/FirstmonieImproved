package firstmob.firstbank.com.firstagent.contract;

public interface CashDepoTransContract {

    interface Presenter {
        void NameEnquiry(String acno);



        void ondestroy();
    }
    interface ILoginView {


        void showToast(String text);

        void setaccname(String name);

        void showProgress();

        void hideProgress();

        void onLoginResult();
    }


}