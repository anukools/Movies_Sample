package com.sample.popularmovies.Jiny;

/**
 * Created by Anukool Srivastav on 4/29/2017.
 */

public class BusEvents {

    public static class HideEvent {

    }

    public static class ShowUIEvent {
        float x;
        float y;
        int gravity;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public int getGravity() {
            return gravity;
        }

        public void setGravity(int gravity) {
            this.gravity = gravity;
        }
    }
}
