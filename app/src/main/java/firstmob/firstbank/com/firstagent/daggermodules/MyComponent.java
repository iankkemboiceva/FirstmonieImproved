package firstmob.firstbank.com.firstagent.daggermodules;

import firstmob.firstbank.com.firstagent.presenter.ActivateAgentPresenter;
import firstmob.firstbank.com.firstagent.presenter.LoginPresenterCompl;

import dagger.Component;
import firstmob.firstbank.com.firstagent.presenter.SignInPresenter;

@Component(modules={ContextModule.class})
public  interface MyComponent {
    void inject(LoginPresenterCompl presenter);
    void inject(ActivateAgentPresenter presenter);
    void inject(SignInPresenter presenter);
}