package firstmob.firstbank.com.firstagent.contract;

import java.util.ArrayList;

import firstmob.firstbank.com.firstagent.adapter.InboxListAdapter;
import firstmob.firstbank.com.firstagent.model.GetCommPerfData;

public interface InboxContract {

    interface Presenter {
        void Inbox(String startdate,String enddate);



        void ondestroy();
    }
    interface ILoginView {


        void showToast(String text);

        void setaccname(String name);

        void setList( ArrayList<GetCommPerfData> inboxlist);

        void showProgress();

        void hideProgress();

        void onLoginResult();
    }


}