package com.example.fabik.parkingapp;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.example.fabik.parkingapp.Printer.PrintManager;
import com.example.fabik.parkingapp.Printer.utils.LogUtil;

import com.mazenrashed.printooth.Printooth;
import com.pos.device.SDKManager;
import com.pos.device.SDKManagerCallback;

/**
 * Creado por Fabian Ardila
 */

public class appInit extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    private void init() {
        System.out.println("MODELO " + Build.MODEL);
        if (Build.MODEL.equals("NEW9220")) {
            appContext = getApplicationContext();
            SDKManager.init(appInit.appContext, new SDKManagerCallback() {
                @Override
                public void onFinish() {
                    LogUtil.e("init SDK success(threadname: " + Thread.currentThread().getName() + ")");
                }
            });
            PrintManager.init(getApplicationContext());
        } else {
            Printooth.INSTANCE.init(this);
        }

    }

}
