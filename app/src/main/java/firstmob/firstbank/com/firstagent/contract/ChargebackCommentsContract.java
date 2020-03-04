package firstmob.firstbank.com.firstagent.contract;

import firstmob.firstbank.com.firstagent.model.ChargebackList;

public interface ChargebackCommentsContract {

    interface Presenter {
        void saveChargeback(String pin,String comments,String iscashgiven,String txrefnum,int chgbckid,String receipt);



        void ondestroy();
    }
    interface ILoginView {


        void showToast(String text);



        void showProgress();


        void hideProgress();

        void goNextPage();
    }


}