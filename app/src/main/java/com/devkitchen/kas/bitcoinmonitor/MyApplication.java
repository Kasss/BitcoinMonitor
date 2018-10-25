package com.devkitchen.kas.bitcoinmonitor;

import android.app.Application;

import com.devkitchen.kas.bitcoinmonitor.modules.ApplicationComponent;
import com.devkitchen.kas.bitcoinmonitor.modules.ApplicationModule;
import com.devkitchen.kas.bitcoinmonitor.modules.DaggerApplicationComponent;


public class MyApplication extends Application{

    private static ApplicationComponent applicationComponent;

    public MyApplication(){

    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }

    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }

}
