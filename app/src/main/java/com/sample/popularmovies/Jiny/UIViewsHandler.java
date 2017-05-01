package com.sample.popularmovies.Jiny;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.sample.popularmovies.R;

/**
 * Created by Anukool Srivastav on 4/29/2017.
 */

public class UIViewsHandler {
    private static String TAG = "UIViewsHandler";
    WindowManager windowManager;

    public static void handleHomePageView(Context context, View view) {
        // get home recycler view
        View recyclerView = view.findViewById(R.id.movies_recycler_view);
        if (recyclerView != null) {
            Log.d(TAG, "RECYCLER VIEW FOUND");
            PointerService.bus.post(new BusEvents.ShowUIEvent());
        }
    }
}
