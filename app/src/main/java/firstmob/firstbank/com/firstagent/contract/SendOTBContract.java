package firstmob.firstbank.com.firstagent.contract;

public interface SendOTBContract {

    interface Presenter {
        void NameEnquiry(String acno,String bankcode);



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