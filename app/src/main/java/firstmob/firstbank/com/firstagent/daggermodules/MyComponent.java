package firstmob.firstbank.com.firstagent.daggermodules;

import firstmob.firstbank.com.firstagent.Activity.AirtimeTransf;
import firstmob.firstbank.com.firstagent.Activity.Airtime_transfirst;
import firstmob.firstbank.com.firstagent.Activity.ChangePinActivity;
import firstmob.firstbank.com.firstagent.Activity.CommisionActivity;
import firstmob.firstbank.com.firstagent.Activity.ConfirmAirtime;
import firstmob.firstbank.com.firstagent.Activity.ConfirmWithdrawal;
import firstmob.firstbank.com.firstagent.Activity.FinalConfAirtimeActivity;
import firstmob.firstbank.com.firstagent.Activity.FinalConfWithdrawActivity;
import firstmob.firstbank.com.firstagent.Activity.FinalConfirmCableTVActivity;
import firstmob.firstbank.com.firstagent.Activity.ForceChangePin;
import firstmob.firstbank.com.firstagent.Activity.ForceResetPin;
import firstmob.firstbank.com.firstagent.Activity.MinistatActivity;
import firstmob.firstbank.com.firstagent.Activity.MyPerfActivity;
import firstmob.firstbank.com.firstagent.Activity.Withdraw_Firsts;
import firstmob.firstbank.com.firstagent.presenter.ActivateAgentPresenter;
import firstmob.firstbank.com.firstagent.presenter.CashDepoPresenter;
import firstmob.firstbank.com.firstagent.presenter.CashDepoTransPresenter;
import firstmob.firstbank.com.firstagent.presenter.ComplaintsPresenter;
import firstmob.firstbank.com.firstagent.presenter.ConfirmCashDepoPresenter;
import firstmob.firstbank.com.firstagent.presenter.ConfirmCashTransPresenter;
import firstmob.firstbank.com.firstagent.presenter.InboxPresenter;
import firstmob.firstbank.com.firstagent.presenter.LogComplaintPresenter;
import firstmob.firstbank.com.firstagent.presenter.LoginPresenterCompl;

import dagger.Component;
import firstmob.firstbank.com.firstagent.presenter.SignInPresenter;
import firstmob.firstbank.com.firstagent.presenter.TransactionProcPresenter;

@Component(modules={ContextModule.class})
public  interface MyComponent {
    void inject(LoginPresenterCompl presenter);
    void inject(ActivateAgentPresenter presenter);
    void inject(SignInPresenter presenter);

    void inject(TransactionProcPresenter presenter);

    void inject(CashDepoPresenter presenter);
    void inject(ConfirmCashDepoPresenter presenter);

    void inject(InboxPresenter presenter);
    void inject(LogComplaintPresenter presenter);
    void inject(ComplaintsPresenter presenter);
    void inject(CashDepoTransPresenter presenter);
    void inject(ConfirmCashTransPresenter presenter);
    void inject(Withdraw_Firsts activity);
    void inject(MyPerfActivity activity);
    void inject(Airtime_transfirst activity);
    void inject(ConfirmAirtime activity);
    void inject(ForceChangePin activity);
    void inject(ForceResetPin activity);
    void inject(ChangePinActivity activity);
    void inject(ConfirmWithdrawal activity);
    void inject(MinistatActivity activity);
    void inject(CommisionActivity activity);
    void inject(FinalConfWithdrawActivity activity);
    void inject(FinalConfAirtimeActivity activity);
    void inject(FinalConfirmCableTVActivity activity);
}