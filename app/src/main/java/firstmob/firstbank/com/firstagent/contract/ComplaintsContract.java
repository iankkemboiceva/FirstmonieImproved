package firstmob.firstbank.com.firstagent.contract;

import java.util.ArrayList;

import firstmob.firstbank.com.firstagent.model.CommisionsJSON;
import firstmob.firstbank.com.firstagent.model.GetCommPerfData;

public interface ComplaintsContract {

    interface Presenter {
        void Complaints(String startdate, String enddate);



        void ondestroy();
    }
    interface ILoginView {


        void showToast(String text);

        void setaccname(String name);

        void setList(ArrayList<CommisionsJSON> inboxlist);

        void showProgress();

        void hideProgress();

        void onLoginResult();
    }


}