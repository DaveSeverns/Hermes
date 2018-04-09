package com.prestigeww.hermes.Model;

import android.app.Application;

public class Hermes extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
