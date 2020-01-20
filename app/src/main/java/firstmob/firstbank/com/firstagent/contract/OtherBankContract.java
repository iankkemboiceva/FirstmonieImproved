package firstmob.firstbank.com.firstagent.contract;

import java.util.ArrayList;

import firstmob.firstbank.com.firstagent.adapter.GetBanksData;
import firstmob.firstbank.com.firstagent.model.GetCommPerfData;

public interface OtherBankContract {

    interface Presenter {
        void BanksList();



        void ondestroy();
    }
    interface ILoginView {


        void showToast(String text);



        void setList(ArrayList<GetBanksData> getbankslist);

        void showProgress();

        void hideProgress();


    }


}