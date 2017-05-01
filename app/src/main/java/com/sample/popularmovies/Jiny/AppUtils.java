package com.sample.popularmovies.Jiny;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Anukool Srivastav on 19/4/16.
 */
public class AppUtils {

    private static Point sScreenSize = null;


    public static int getScreenWidth(Context context) {
        fetchScreenSize(context);
        return sScreenSize.x;
    }

    public static int getScreenHeight(Context context) {
        fetchScreenSize(context);
        return sScreenSize.y;
    }

    private static void fetchScreenSize(Context context) {
        int softButtonsHeight = 0;
        if (sScreenSize != null) return;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Configuration configuration = context.getResources().getConfiguration();

        boolean isPortrait = true;
        int rotation = display.getRotation();

        sScreenSize = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            DisplayMetrics absoluteMetrics = new DisplayMetrics();
            display.getRealMetrics(absoluteMetrics);
            softButtonsHeight = absoluteMetrics.heightPixels - metrics.heightPixels;
            sScreenSize.x = metrics.widthPixels;
            sScreenSize.y = metrics.heightPixels;
        } else {
            display.getSize(sScreenSize);
        }

        if (sScreenSize.x > sScreenSize.y) isPortrait = false;
        if (!isPortrait) {
            int tempX = sScreenSize.x;
            int tempY = sScreenSize.y;
            sScreenSize.x = tempY + softButtonsHeight;
            sScreenSize.y = tempX - softButtonsHeight;
        }

    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    static SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-mm-dd");
    static SimpleDateFormat writeFormat = new SimpleDateFormat("MMM dd, yyyy");

    public static String formateDate(String dateFormat){
        Date date = null;
        try {
            date = readFormat.parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return writeFormat.format(date);
    }

}
