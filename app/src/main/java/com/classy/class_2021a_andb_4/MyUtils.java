package com.classy.class_2021a_andb_4;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class MyUtils {

    public static void sendAttachedMail(Context activityContext, String filePath, String textToMail) {
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AppFiles");
        boolean success = false;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        //File dir = new File(getExternalFilesDir(null) + File.separator + "AppFiles");
        File file = new File(folder.getPath() + File.separator + "table_of_shifts" + ".csv");

        //File file = new File(filePath);

        Uri csvUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            csvUri = FileProvider.getUriForFile(activityContext, activityContext.getPackageName() + ".provider", file);
        } else {
            //doesn't work in API 24+
            csvUri  =   Uri.fromFile(file);
        }


        final Intent emailIntent1 = new Intent(Intent.ACTION_SEND);
        emailIntent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent1.putExtra(Intent.EXTRA_STREAM, csvUri);
        emailIntent1.putExtra(Intent.EXTRA_EMAIL  , new String[]{"guyskv@gmail.com"});
        emailIntent1.putExtra(Intent.EXTRA_SUBJECT, "[" + "iSchedule" + "] - " + " CSV File");
        emailIntent1.putExtra(Intent.EXTRA_TEXT   , textToMail + "\n\n\nSent from iSchedule App");
        emailIntent1.putExtra(Intent.EXTRA_CC, new String[] {""});
        emailIntent1.setData(Uri.parse("mailto:")); // or just "mailto:" for blank
        emailIntent1.setType("text/html");
        activityContext.startActivity(Intent.createChooser(emailIntent1, "Send email using"));
    }
}
