package com.nathb.goodnightlukie.service.timer;

import android.os.Handler;

import com.nathb.goodnightlukie.GoodNightApp;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class TimerManager {

    public interface TimerFinishedListener {
        void onTimerFinished();
    }

    private long mStartTimestamp;
    private long mDurationMillis;
    private boolean mIsTimerRunning;
    private TimerRunnable mTimerRunnable;
    private final CopyOnWriteArraySet<TimerFinishedListener> mListeners;

    @Inject Handler mHandler;

    public TimerManager() {
        GoodNightApp.inject(this);
        mListeners = new CopyOnWriteArraySet<>();
        mTimerRunnable = new TimerRunnable();
    }

    public void startTimer(long durationMillis) {
        mIsTimerRunning = true;
        mStartTimestamp = System.currentTimeMillis();
        mDurationMillis = durationMillis;
        mHandler.removeCallbacks(mTimerRunnable);
        mHandler.postDelayed(mTimerRunnable, mDurationMillis);
    }

    public void stopTimer() {
        mIsTimerRunning = false;
        mHandler.removeCallbacks(mTimerRunnable);
    }

    public void addListener(TimerFinishedListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(TimerFinishedListener listener) {
        mListeners.remove(listener);
    }

    public int getSecondsRemaining() {
        final long elapsed = System.currentTimeMillis() - mStartTimestamp;
        return (int) TimeUnit.MILLISECONDS.toSeconds(mDurationMillis - elapsed);
    }

    public boolean isTimerRunning() {
        return mIsTimerRunning;
    }

    private class TimerRunnable implements Runnable {
        @Override
        public void run() {
            mIsTimerRunning = false;
            for (TimerFinishedListener listener : mListeners) {
                listener.onTimerFinished();
            }
        }
    }
}
