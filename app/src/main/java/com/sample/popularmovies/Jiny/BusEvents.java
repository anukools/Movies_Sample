package com.sample.popularmovies.Jiny;

/**
 * Created by Anukool Srivastav on 4/29/2017.
 */

public class BusEvents {

    public static class HideEvent {

    }

    public static class RemoveEvent {

    }

    public static class ShowUIEvent {
        float x;
        float y;
        int soundResId;
        int gravity;

        public int getSoundResId() {
            return soundResId;
        }

        public void setSoundResId(int soundResId) {
            this.soundResId = soundResId;
        }

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
