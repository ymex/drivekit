/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 * <p>
 * Email:ymex@foxmail.com  (www.ymex.cn)
 * @author ymex
 * date: 16/4/21
 */
package cn.ymex.kitx.kits;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public final class Kits {
    private static Context application;

    private Kits() {
        throw new RuntimeException("Kits not allow instance");
    }


    public static void create(Context context) {
        init(context);
    }

    private static void init(Context context) {
        if (application instanceof Application) {
            application = context;
        } else {
            application = context.getApplicationContext();
        }
        Device.synKitId();
    }



    public static Application getApplication() {
        Empty.checkNull(application, "application is null!");
        return (Application) application;
    }

    public static void log(String text) {
        Log.e("Kits.E", text);
    }
}
