package com.sample.popularmovies.app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.sample.popularmovies.BuildConfig;
import com.sample.popularmovies.Jiny.AppUtils;
import com.sample.popularmovies.Jiny.AysncServices.AsyncResponseInterface;
import com.sample.popularmovies.Jiny.AysncServices.TriggerViewEventAsyncTask;
import com.sample.popularmovies.Jiny.BusEvents;
import com.sample.popularmovies.Jiny.PointerService;
import com.sample.popularmovies.R;
import com.sample.popularmovies.services.models.movieapi.Result;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

/**
 * Created by Anukool Srivastav on 16/4/16.
 */
public class MovieDetailsActivity extends BaseActivity implements MovieDetailsFragment.IMovieTrailerSetListener,
        AppBarLayout.OnOffsetChangedListener {

    MovieDetailsFragment movieDetailsFragment;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.movie_image_banner)
    ImageView mMovieBannerImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton favouriteFab;
    private Result movie;
    private String youTubeVideoId;
    private FavoriteMoviesManager favoriteMoviesManager;


    private static final int PERCENTAGE_TO_SHOW_IMAGE = 90;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setBackNavigationIcon();
        init();
        if (savedInstanceState == null) {
            movieDetailsFragment = new MovieDetailsFragment();
            Bundle bundle = new Bundle();
            movieDetailsFragment.setArguments(bundle);
            bundle.putParcelable(IBundleParams.RESULT_OBJ, getIntent().getParcelableExtra(IBundleParams.RESULT_OBJ));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, movieDetailsFragment)
                    .commit();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if(favouriteFab.getVisibility() == View.VISIBLE && favouriteFab.getX() != 0) {
            sendViewAnimateEvent();
        }else {
            BusEvents.HideEvent hideEvent = new BusEvents.HideEvent();
            hideEvent.setHideJinyIcon(false);
            PointerService.bus.post(hideEvent);
        }
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;

                ViewCompat.animate(favouriteFab).scaleY(0).scaleX(0).start();
                /**
                 * Realize your any behavior for FAB here!
                 **/
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(favouriteFab).scaleY(1).scaleX(1).start();
                /**
                 * Realize your any behavior for FAB here!
                 **/
            }
        }
    }

    void init() {
        favoriteMoviesManager = FavoriteMoviesManager.create(this);
        appBarLayout.addOnOffsetChangedListener(this);
        setDataToViews();
    }

    void setDataToViews() {
        movie = (Result) getIntent().getParcelableExtra(IBundleParams.RESULT_OBJ);
        if (favoriteMoviesManager.isFavorite(movie)) {
            updateFavouriteFab(true);
        }
        mCollapsingToolbarLayout.setTitle(movie.getOriginalTitle());
        Picasso.with(this).load(BuildConfig.IMAGE_BASE_URL_342 + movie.getBackdropPath()).into(mMovieBannerImage, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap myBitmap = ((BitmapDrawable) mMovieBannerImage.getDrawable()).getBitmap();
                if (myBitmap != null && !myBitmap.isRecycled()) {
                    Palette.from(myBitmap).generate(paletteListener);
                }
            }

            @Override
            public void onError() {

            }
        });

    }

    Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
        public void onGenerated(Palette palette) {
            int defaultColor = 0x000000;
            int vibrant = palette.getVibrantColor(defaultColor);
            int vibrantLight = palette.getLightVibrantColor(defaultColor);
            int vibrantDark = palette.getDarkVibrantColor(defaultColor);
            mCollapsingToolbarLayout.setBackgroundColor(vibrant);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mCollapsingToolbarLayout.setStatusBarScrimColor(vibrant);
                mCollapsingToolbarLayout.setContentScrimColor(vibrant);
            }
        }
    };

    @OnClick({R.id.collapsing_toolbar, R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collapsing_toolbar:
                if (youTubeVideoId != null) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + youTubeVideoId));
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // launch default browser if youtube app is not installed in device
                        openInDefaultBrowser();
                    }
                }
                break;
            case R.id.fab:
                if (favoriteMoviesManager.isFavorite(movie)) {
                    favoriteMoviesManager.remove(movie);
                    updateFavouriteFab(false);
                    Toast.makeText(MovieDetailsActivity.this, movie.getOriginalTitle() + " has been removed from your favorites", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MovieDetailsActivity.this, movie.getOriginalTitle() + " has been added to your favorites", Toast.LENGTH_SHORT).show();
                    favoriteMoviesManager.add(movie);
                    updateFavouriteFab(true);
                }
                break;
        }
    }

    private void openInDefaultBrowser() {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(" https://www.youtube.com/watch?v=" + youTubeVideoId));
            startActivity(browserIntent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onLoad(String youTubeVideoId) {
        this.youTubeVideoId = youTubeVideoId;
    }

    private void updateFavouriteFab(boolean isFavourite) {
        if (isFavourite) {
            favouriteFab.setImageResource(R.drawable.icon_favourite);
        } else {
            favouriteFab.setImageResource(R.drawable.icon_unfavourite);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        sendTriggerBasedEvent();

        favouriteFab.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // hide when the view changed
        PointerService.bus.post(new BusEvents.HideEvent());
    }


    private void sendTriggerBasedEvent() {
        TriggerViewEventAsyncTask subscribe = new TriggerViewEventAsyncTask(asyncResponseInterface);
        subscribe.execute("2"); //  2 for details screen
    }

    ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            // Layout has happened here.

            sendViewEvent();

            // Don't forget to remove your listener when you are done with it.
            favouriteFab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    };


    AsyncResponseInterface asyncResponseInterface = new AsyncResponseInterface() {
        @Override
        public void onSuccess(String response) {
            Log.e("Response :", response + "");
            if (response != null && response.equalsIgnoreCase("Details")) {
                sendViewEvent();
            }
        }
    };

    private void sendViewAnimateEvent() {
        BusEvents.AnimateEvent animateEvent = new BusEvents.AnimateEvent();
        Rect rect = new Rect();
        favouriteFab.getGlobalVisibleRect(rect);

        int heightDiff= (int) (maxHeight - rect.exactCenterY());

        Log.e("Values : ", "---- X -  " + rect.exactCenterX() + " -- Y - " + rect.exactCenterY());

        animateEvent.setX((int) ((AppUtils.getScreenWidth(getApplicationContext()) - rect.exactCenterX()) / 2));
        animateEvent.setY((int) ((AppUtils.getScreenHeight(getApplicationContext()) - rect.exactCenterY()) / 2) - heightDiff);
        PointerService.bus.post(animateEvent);
    }

    private int maxHeight = 0;
    private void sendViewEvent() {
        if (favouriteFab.getVisibility() == View.VISIBLE) {
            // post event bus to show pointer
            BusEvents.ShowUIEvent event = new BusEvents.ShowUIEvent();

            Rect rect = new Rect();
            favouriteFab.getGlobalVisibleRect(rect);

            maxHeight = (int) rect.exactCenterY();

            event.setX((AppUtils.getScreenWidth(getApplicationContext()) - rect.exactCenterX()) / 2);
            event.setY((AppUtils.getScreenHeight(getApplicationContext()) - rect.exactCenterY()) / 2);
            event.setSoundResId(R.raw.feedback_2);
            event.setGravity(Gravity.TOP | Gravity.END);
            PointerService.bus.post(event);
        }
    }
}

