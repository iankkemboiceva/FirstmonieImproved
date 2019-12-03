package firstmob.firstbank.com.firstagent.daggermodules;

import firstmob.firstbank.com.firstagent.presenter.ActivateAgentPresenter;
import firstmob.firstbank.com.firstagent.presenter.CashDepoPresenter;
import firstmob.firstbank.com.firstagent.presenter.ConfirmCashDepoPresenter;
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
    void inject(CashDepoPresenter presenter);
    void inject(ConfirmCashDepoPresenter presenter);
    void inject(TransactionProcPresenter presenter);
    void inject(InboxPresenter presenter);
    void inject(LogComplaintPresenter presenter);
}