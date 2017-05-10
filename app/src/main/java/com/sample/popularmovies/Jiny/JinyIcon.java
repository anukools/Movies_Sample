package com.sample.popularmovies.Jiny;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sample.popularmovies.Jiny.AysncServices.JinyIconView;
import com.sample.popularmovies.R;

/**
 * Created by Anukool Srivastav on 5/10/2017.
 */

public class JinyIcon {
    private WindowManager windowManager;
    private Context context;
    private LayoutInflater inflater;

    private WindowManager.LayoutParams layoutParams;

    private JinyIconView jinyIconView;

    // dimensions
    private int totalScreenWidth;
    private int totalScreenHeight;
    private int statusBarHeight;

    private ImageView jinyView;
    private int jinyRightMargin = 150;

    public JinyIcon(WindowManager windowManager, Context context, LayoutInflater inflater) {
        this.windowManager = windowManager;
        this.context = context;
        this.inflater = inflater;

        totalScreenWidth = AppUtils.getScreenWidth(context);
        totalScreenHeight = AppUtils.getScreenHeight(context);
        statusBarHeight = AppUtils.getStatusBarHeight(context);

        try {
            this.createBubble();
            jinyIconView = new JinyIconView(this, windowManager, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createBubble() {
        jinyView = (ImageView) inflater.inflate(R.layout.jiny_icon, null, false);
        jinyView.setVisibility(View.GONE);

        layoutParams = new WindowManager.LayoutParams(
                100,
                100,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP | Gravity.START;
        layoutParams.x = totalScreenWidth - jinyRightMargin;
        layoutParams.y = totalScreenHeight / 3;
        layoutParams.dimAmount = 0.6f;
        layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        windowManager.addView(jinyView, layoutParams);

    }


    public void hide() {
        if (jinyView != null) {
            if (jinyView.getVisibility() == View.VISIBLE) {
                jinyView.setVisibility(View.GONE);
            }
        }
    }

    public void show() {
        try {
            if (jinyView != null) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        jinyView.setVisibility(View.VISIBLE);

                        layoutParams.x = totalScreenWidth - jinyRightMargin;
                        layoutParams.y = totalScreenHeight / 3 - 100;
                        jinyIconView.moveJinyIconDirectly(layoutParams.x, layoutParams.y);

                        jinyView.bringToFront();

                    }
                }, 500);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removePointer() {
        if (jinyView != null)
            windowManager.removeView(jinyView);
    }

    public View getView() {
        return jinyView;
    }
}

