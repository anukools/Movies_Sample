package com.sample.popularmovies.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.popularmovies.Jiny.AppUtils;
import com.sample.popularmovies.Jiny.AysncServices.AsyncResponseInterface;
import com.sample.popularmovies.Jiny.AysncServices.TriggerViewEventAsyncTask;
import com.sample.popularmovies.Jiny.BusEvents;
import com.sample.popularmovies.R;
import com.sample.popularmovies.services.models.movieapi.Result;
import com.sample.popularmovies.Jiny.PointerService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anukool Srivastav on 16/4/16.
 */
public class MoviesActivity extends BaseActivity implements MoviesFragment.OnMovieSelectListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Nullable
    boolean isTwoPane;

    Intent uiServiceIntent;

    public final static int REQUEST_CODE = 10101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        isTwoPane = findViewById(R.id.movie_details_container) != null;

        checkDrawOverlayPermission();

    }

    /**
     * Method used to set the toolbar title
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        mToolbarTitle.setText(title);
    }


    public String getToolbarTitle() {
        return mToolbarTitle.getText().toString();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onMovieSelected(View v, Result item) {
        String transitionName = getString(R.string.transition_string);
        ImageView viewStart = null;
        if (v != null) {
            viewStart = (ImageView) v.findViewById(R.id.movie_image);
        }
        if (!isTwoPane) {
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra(IBundleParams.RESULT_OBJ, (Parcelable) item);
            if (viewStart != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options =

                        ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                                viewStart,
                                transitionName
                        );
                ActivityCompat.startActivity(this, intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        } else {
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(IBundleParams.RESULT_OBJ, (Parcelable) item);
            movieDetailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, movieDetailsFragment)
                    .commit();
        }

    }

    public boolean isTwoPaneContainer() {
        return isTwoPane;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // hide when the view changed
        PointerService.bus.post(new BusEvents.RemoveEvent());

        if (uiServiceIntent != null)
            stopService(uiServiceIntent);


    }

    /**
     * Checks for draw over the apps permisson
     *
     * @return
     */
    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startUIService();
            return true;
        } else {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
                return false;
            } else {
                startUIService();
                return true;
            }
        }
    }

    /**
     * if permission is granted then start UI service
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startUIService();
            }
        }
    }

    private void startUIService() {
        // Start the Ui Service
        uiServiceIntent = new Intent(this, PointerService.class);
        startService(uiServiceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
