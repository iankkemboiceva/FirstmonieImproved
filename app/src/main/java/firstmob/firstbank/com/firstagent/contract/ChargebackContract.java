package firstmob.firstbank.com.firstagent.contract;

import firstmob.firstbank.com.firstagent.model.ChargebackList;

public interface ChargebackContract {

    interface Presenter {
        void getChargebackDetails(int chgbckid);



        void ondestroy();
    }
    interface ILoginView {


        void showToast(String text);

        void setResultText(ChargebackList cglist);

        void showProgress();
        void hidebutton();

        void hideProgress();

        void onLoginResult();
    }


}