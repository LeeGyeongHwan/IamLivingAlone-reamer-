package com.LSK.iamlivingalone;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class IamLivingAlone extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());
    }
}
