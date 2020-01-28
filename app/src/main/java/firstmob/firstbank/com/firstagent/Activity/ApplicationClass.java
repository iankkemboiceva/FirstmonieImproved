package firstmob.firstbank.com.firstagent.Activity;

import android.app.Application;
import android.content.ContextWrapper;

import androidx.multidex.MultiDex;

import androidx.multidex.MultiDexApplication;
import firstmob.firstbank.com.firstagent.daggermodules.ContextModule;
import firstmob.firstbank.com.firstagent.daggermodules.DaggerMyComponent;
import firstmob.firstbank.com.firstagent.daggermodules.MyComponent;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.pixplicity.easyprefs.library.Prefs;


public class ApplicationClass extends MultiDexApplication {
    private static MyComponent myComponent;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        myComponent = DaggerMyComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .build();

        FirebaseApp.initializeApp(this);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);



        AppCenter.start(this, "9cd75003-bfb8-42bf-a4c4-343777b90400",
                Analytics.class, Crashes.class);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/poppins.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        MultiDex.install(this);
        //  initObjectGraph(new Fingerprin


        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    public static MyComponent getMyComponent() {
        return myComponent;
    }
}