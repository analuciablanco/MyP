package com.example.myp;

import android.app.Application;
import com.google.firebase.FirebaseApp;

// Clase que inicializa FireBase
public class FirebaseActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
