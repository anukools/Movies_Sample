package com.sample.popularmovies.Jiny;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sample.popularmovies.R;

/**
 * Created by Anukool Srivastav on 4/29/2017.
 */

public class PointerIcon {

    private String TAG = this.getClass().getSimpleName();


    private WindowManager windowManager;
    private Context context;
    private LayoutInflater inflater;

    private WindowManager.LayoutParams layoutParams;


    // dimensions
    private int totalScreenWidth;
    private int totalScreenHeight;
    private int statusBarHeight;

    private ImageView bubbleView;

    public PointerIcon(WindowManager windowManager, Context context, LayoutInflater inflater) {
        this.windowManager = windowManager;
        this.context = context;
        this.inflater = inflater;

        totalScreenWidth = AppUtils.getScreenWidth(context);
        totalScreenHeight = AppUtils.getScreenHeight(context);
        statusBarHeight = AppUtils.getStatusBarHeight(context);

        try {
            this.createBubble();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createBubble() {
        bubbleView = (ImageView) inflater.inflate(R.layout.dummy_layout, null, false);
        bubbleView.setBackgroundResource(R.drawable.pointer_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) bubbleView.getBackground();
        animationDrawable.start();
        animationDrawable.setOneShot(false);

        layoutParams = new WindowManager.LayoutParams(
                100,
                100,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP | Gravity.END;
        layoutParams.x = 0;
        layoutParams.y = totalScreenHeight / 2 - 100;
        layoutParams.dimAmount = 0.6f;
        layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        windowManager.addView(bubbleView, layoutParams);

    }


    public void hide() {
        if (bubbleView != null) {
            if (bubbleView.getVisibility() == View.VISIBLE) {
                bubbleView.setVisibility(View.GONE);
            }
        }
    }

    public void show() {
        try {
            if (bubbleView != null) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        bubbleView.setVisibility(View.VISIBLE);
                    }
                }, 1000);


//                layoutParams.x = 0;
//                layoutParams.y = totalScreenHeight / 2 - 100;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
