package com.aceplus.rmsproject.rmsproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;



import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by macmininew on 12/11/16.
 */

public class CustomExceptionHandler {

    private static CustomExceptionHandler INSTANCE;

    CustomExceptionHandler() {
    }

    public static CustomExceptionHandler newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomExceptionHandler();
        }
        return INSTANCE;
    }

    public static void traceUnchagedException(final Context context) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                StringWriter writer = new StringWriter();
                PrintWriter pw = new PrintWriter(writer);
                throwable.printStackTrace(pw);
                writer.toString();
                Intent in = new Intent(context, SendExceptionActivity.class);
//                in.putExtra(Constant.KEY_EXCEPTION, writer.toString());
                in.putExtra("ConstantKey", writer.toString());
                context.startActivity(in);
                ((Activity) context).finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }


}
