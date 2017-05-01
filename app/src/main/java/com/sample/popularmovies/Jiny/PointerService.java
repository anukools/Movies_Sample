package com.sample.popularmovies.Jiny;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.sample.popularmovies.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Anukool Srivastav on 4/29/2017.
 */

public class PointerService extends Service {
    public static Bus bus = new Bus(ThreadEnforcer.ANY);
    private String TAG = this.getClass().toString();
    private PointerIcon pointerIcon;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getBaseContext(), R.style.AppTheme);
            this.context = contextThemeWrapper;

            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            LayoutInflater inflater = (LayoutInflater) contextThemeWrapper.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            pointerIcon = new PointerIcon(windowManager, context, inflater);
//            pointerIcon.hide();

            PointerService.bus.register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (startId == Service.START_STICKY) {
            return super.onStartCommand(intent, flags, startId);
        } else {
            return Service.START_NOT_STICKY;
        }
    }


    @Subscribe
    public void showPointerUIEvent(BusEvents.ShowUIEvent event) {
        pointerIcon.show();
    }

    @Subscribe
    public void hidePointerUIEvent(BusEvents.HideEvent event) {
        pointerIcon.hide();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        PointerService.bus.unregister(this);
    }
}