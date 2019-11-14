package firstmob.firstbank.com.firstagent.contract;

public interface TransactionProcessingContract {

    interface Presenter {
        void IntraDepoBankResp(String params);



        void ondestroy();
    }
    interface ILoginView {


        void showToast(String text);

        void setstatus(String name);

        void setdesc(String name);

        void showProgress();

        void onErrorResult(String errormsg);

        void hideProgress();

        void CashDepoResult(String refcodee,String datetime,String agcmsn,String totfee);
    }


}