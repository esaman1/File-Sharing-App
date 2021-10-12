package com.datasharing.Application;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.datasharing.Const.Constant;
import com.datasharing.Model.SendFileModel;

import java.util.ArrayList;


public class MyApplicationClass extends MultiDexApplication {
    private static final String ONESIGNAL_APP_ID = "6d27f147-b8e1-4cea-8e9a-0a2015a11662";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    ArrayList<SendFileModel> filesList = new ArrayList<>();
//    private static AppOpenManager appOpenManager;

    @SuppressWarnings("EmptyMethod")
    @Override
    public void onCreate() {
        super.onCreate();

        // Admob Ads.
//        MobileAds.initialize(
//                this,
//                initializationStatus -> {});
//
//        appOpenManager = new AppOpenManager(this);
//
//        // Enable verbose OneSignal logging to debug issues if needed.
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//
//        // OneSignal Initialization
//        OneSignal.initWithContext(this);
//        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(MyApplicationClass.this);
    }

    public void setMap(ArrayList<SendFileModel> filesList) {
        this.filesList = filesList;
    }

    public void reset() {
        this.filesList.clear();
        filesList = new ArrayList<>();
        Constant.filePaths.clear();
        Constant.FileName.clear();
        Constant.filePaths = new ArrayList<>();
        Constant.FileName = new ArrayList<>();
    }

    public ArrayList<SendFileModel> getFilesList() {
        return filesList;
    }
}
