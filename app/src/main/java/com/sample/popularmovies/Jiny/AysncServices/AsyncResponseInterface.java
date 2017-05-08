package com.sample.popularmovies.Jiny.AysncServices;

/**
 * Created by Anukool Srivastav on 5/8/2017.
 */

public interface AsyncResponseInterface {
    void onSuccess(String response);
    void onFailure(String failure);
}
