package com.example.myp;

import android.app.Application;
import com.google.firebase.FirebaseApp;

// Class to initialize FireBase
public class FirebaseActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
