package com.sample.popularmovies.Jiny;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.sample.popularmovies.R;

/**
 * Created by Anukool Srivastav on 4/29/2017.
 */

public class UIViewsHandler {
    private static String TAG = "UIViewsHandler";

    public static void handleHomePageView(Context context, View view) {
        // get home recycler view
        View recyclerView = view.findViewById(R.id.movies_recycler_view);
        if (recyclerView != null) {
            Log.d(TAG, "RECYCLER VIEW FOUND");
            // post event bus to show pointer
            BusEvents.ShowUIEvent event = new BusEvents.ShowUIEvent();
            event.setX(50);
            event.setY(AppUtils.getScreenHeight(context) / 2 - 100);
            event.setGravity(Gravity.TOP | Gravity.END);
            PointerService.bus.post(event);
        }
    }
}
