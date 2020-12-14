package cn.edu.bistu.cs.se.mywordsapp;

import android.content.Context;


public class Application extends android.app.Application {
    private static Context context;
    public static Context getContext(){
        return Application.context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Application.context=getApplicationContext();
    }
}
