package com.jam.pinkenew;

import android.app.Application;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;

import cn.bmob.v3.Bmob;

/**
 * Created by augustinus on 16/8/18.
 */

public class MyApplication extends Application {
    private final String APPID="d06224c2918c9171551b77a2ce70f88c";
    @Override
    public void onCreate() {

        super.onCreate();
        Fresco.initialize(getApplicationContext());
        Bmob.initialize(getApplicationContext(), APPID);
//        Intent intent=new Intent(getApplicationContext(), PinKePushService.class);
//        startService(intent);
//        System.out.println("application create");
    }
}
