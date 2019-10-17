package firstmob.firstbank.com.firstagent.daggermodules;

import firstmob.firstbank.com.firstagent.presenter.LoginPresenterCompl;

import dagger.Component;

@Component(modules={ContextModule.class})
public  interface MyComponent {
    void inject(LoginPresenterCompl presenter);
}