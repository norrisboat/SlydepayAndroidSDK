package com.apps.norris.slydepayandroidrest;

import android.app.Application;

import com.apps.norris.paywithslydepay.core.SlydepayPayment;

/**
 * Created by norrisboateng on 6/17/17.
 */

public class ApplicationSingleton extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new SlydepayPayment(this).initCredentials("dzigbos@gmail.com","1492641173560");
    }
}
