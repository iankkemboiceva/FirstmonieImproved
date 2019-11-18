package firstmob.firstbank.com.firstagent.contract;

public interface InboxContract {

    interface Presenter {
        void Inbox(String startdate,String enddate);



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