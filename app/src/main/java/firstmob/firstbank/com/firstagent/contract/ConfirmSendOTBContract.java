package firstmob.firstbank.com.firstagent.contract;

public interface ConfirmSendOTBContract {

    interface Presenter {
        void getFeeSec(String amou);



        void ondestroy();
    }
    interface ILoginView {


        void showToast(String text);

        void settextfee(String name, Boolean fee);

        void showProgress();
        void hidebutton();

        void hideProgress();

        void onLoginResult();
    }


}