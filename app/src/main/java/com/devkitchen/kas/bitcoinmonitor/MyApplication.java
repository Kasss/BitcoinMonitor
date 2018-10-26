package com.devkitchen.kas.bitcoinmonitor;

import android.app.Application;

import com.devkitchen.kas.bitcoinmonitor.modules.ApplicationComponent;
import com.devkitchen.kas.bitcoinmonitor.modules.ApplicationModule;
import com.devkitchen.kas.bitcoinmonitor.modules.DaggerApplicationComponent;

/**
 * created by Kassen Dauren 25.10.2018
 * last update 26.10.2018
 */


public class MyApplication extends Application {

    private static ApplicationComponent applicationComponent;

    public MyApplication() {

    }

    /**
     *
     * Dagger application component implementation
     * @author Kassen Dauren
     * */
    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
