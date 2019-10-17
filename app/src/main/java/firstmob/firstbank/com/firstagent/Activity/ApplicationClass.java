package firstmob.firstbank.com.firstagent.Activity;

import android.app.Application;
import android.content.ContextWrapper;

import androidx.multidex.MultiDex;
import firstmob.firstbank.com.firstagent.daggermodules.ContextModule;
import firstmob.firstbank.com.firstagent.daggermodules.DaggerMyComponent;
import firstmob.firstbank.com.firstagent.daggermodules.MyComponent;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import com.pixplicity.easyprefs.library.Prefs;


public class ApplicationClass extends Application {
    private static  MyComponent myComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        myComponent = DaggerMyComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .build();






        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Light.otf")
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

    public static    MyComponent getMyComponent() {
        return myComponent;
    }
}