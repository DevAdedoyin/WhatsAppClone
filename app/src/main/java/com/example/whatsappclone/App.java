package com.example.whatsappclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("86Tjp2ZVXZsoImGmJpGorejVsIxjC13qkgueGS1u")
                // if desired
                .clientKey("Mk0dxxX0sNaMrgclM1kP5bfLN0OGDVf41I7fJx22")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }

}
