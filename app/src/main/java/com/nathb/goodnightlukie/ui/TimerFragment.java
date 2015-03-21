package com.nathb.goodnightlukie.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nathb.goodnightlukie.GoodNightApp;
import com.nathb.goodnightlukie.R;
import com.nathb.goodnightlukie.service.sound.SoundManager;
import com.nathb.goodnightlukie.service.timer.TimerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TimerFragment extends Fragment {

    private static final String START = "START";
    private static final String STOP = "STOP";
    private static final String ZERO = "0";

    @Inject SoundManager mSoundManager;
    @Inject TimerManager mTimerManager;

    @InjectView(R.id.Hour) TextView mHourView;
    @InjectView(R.id.Minute1) TextView mMinute1View;
    @InjectView(R.id.Minute2) TextView mMinute2View;
    @InjectView(R.id.Second1) TextView mSecond1View;
    @InjectView(R.id.Second2) TextView mSecond2View;
    @InjectView(R.id.SecondLabel) TextView mSecondLabel;
    @InjectView(R.id.StartStopButton) TextView mStartStopButton;
    @InjectView(R.id.BackspaceButton) ImageView mBackspaceButton;
    @InjectView(R.id.TimerInputSection) LinearLayout mTimerInputSection;
    @InjectView(R.id.TimerValueSection) LinearLayout mTimerValueSection;

    private List<TextView> mTimerValues;
    private int mCurrentTimerDigitPosition = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public TimerFragment() { }

    public static TimerFragment newInstance() {
        return new TimerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_timer, container, false);
        GoodNightApp.inject(this);
        ButterKnife.inject(this, view);
        mTimerValues = new ArrayList<>();
        mTimerValues.add(mMinute2View);
        mTimerValues.add(mMinute1View);
        mTimerValues.add(mHourView);
        mTimerManager.addListener(mTimerFinishedListener);
        updateDisplayMode();

        if (mTimerManager.isTimerRunning()) {
            mHandler.post(mUpdateDurationRunnable);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimerManager.removeListener(mTimerFinishedListener);
    }

    @OnClick({
            R.id.OneButton, R.id.TwoButton, R.id.ThreeButton,
            R.id.FourButton, R.id.FiveButton, R.id.SixButton,
            R.id.SevenButton, R.id.EightButton, R.id.NineButton,
            R.id.ZeroButton
    })
    void onNumberButtonClicked(TextView numberButton) {
        if (mCurrentTimerDigitPosition < mTimerValues.size()) {
            // Shift previous numbers left
            for (int i = mCurrentTimerDigitPosition; i > 0; i--) {
                setTimerDigitValue(i, getTimerDigitValue(i - 1));
            }

            // Set current number in right most position
            setTimerDigitValue(0, String.valueOf(numberButton.getText().toString()));

            mCurrentTimerDigitPosition++;
        }
    }

    @OnClick(R.id.BackspaceButton)
    void onBackspace() {
        if (mCurrentTimerDigitPosition > 0) {
            mCurrentTimerDigitPosition--;
            setTimerDigitValue(mCurrentTimerDigitPosition, ZERO);
        }
    }

    @OnClick(R.id.StartStopButton)
    void toggleTimer() {
        if (mTimerManager.isTimerRunning()) {
            mSoundManager.stop();
            mTimerManager.stopTimer();
            onTimerStopped();
        } else {
            final int timerDuration = getDuration();
            if (timerDuration > 0) {
                mSoundManager.play();
                mTimerManager.startTimer(TimeUnit.SECONDS.toMillis(timerDuration));
                onTimerStarted();
            }
        }
    }

    private void onTimerStarted() {
        updateDisplayMode();
        mHandler.post(mUpdateDurationRunnable);
    }

    private void onTimerStopped() {
        mHandler.removeCallbacks(mUpdateDurationRunnable);
        resetDuration();
        updateDisplayMode();
    }

    private void resetDuration() {
        mHourView.setText(ZERO);
        mMinute1View.setText(ZERO);
        mMinute2View.setText(ZERO);
        mCurrentTimerDigitPosition = 0;
    }

    private void updateDisplayMode() {
        final int layoutWeight;
        if (mTimerManager.isTimerRunning()) {
            mSecond1View.setVisibility(View.VISIBLE);
            mSecond2View.setVisibility(View.VISIBLE);
            mSecondLabel.setVisibility(View.VISIBLE);
            mBackspaceButton.setVisibility(View.GONE);
            mTimerInputSection.setVisibility(View.GONE);
            mStartStopButton.setText(STOP);
            mTimerValueSection.setHorizontalGravity(Gravity.CENTER);
            layoutWeight = 4;
        } else {
            mSecond1View.setVisibility(View.GONE);
            mSecond2View.setVisibility(View.GONE);
            mSecondLabel.setVisibility(View.GONE);
            mBackspaceButton.setVisibility(View.VISIBLE);
            mTimerInputSection.setVisibility(View.VISIBLE);
            mStartStopButton.setText(START);
            mTimerValueSection.setHorizontalGravity(Gravity.LEFT);
            layoutWeight = 1;
        }

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, layoutWeight);
        mTimerValueSection.setLayoutParams(params);
        mTimerValueSection.requestLayout();
    }

    private int getDuration() {
        final int hours = Integer.valueOf(mHourView.getText().toString());
        final int minutes = Integer.valueOf(mMinute1View.getText().toString() +
                mMinute2View.getText().toString());
        return (hours * 3600) + (minutes * 60);
    }

    private void setDuration(int durationSeconds) {
        final int seconds = durationSeconds % 60;
        final int totalMinutes = durationSeconds / 60;
        final int minutes = totalMinutes % 60;
        final int hours = totalMinutes / 60;

        final String paddedMinutes = String.format("%02d", minutes);
        final String paddedSeconds = String.format("%02d", seconds);

        mHourView.setText(String.valueOf(hours));
        mMinute1View.setText(String.valueOf(paddedMinutes.charAt(0)));
        mMinute2View.setText(String.valueOf(paddedMinutes.charAt(1)));
        mSecond1View.setText(String.valueOf(paddedSeconds.charAt(0)));
        mSecond2View.setText(String.valueOf(paddedSeconds.charAt(1)));
    }

    private String getTimerDigitValue(int position) {
        final TextView timerDigitView = mTimerValues.get(position);
        return timerDigitView.getText().toString();
    }

    private void setTimerDigitValue(int position, String value) {
        final TextView timerDigitView = mTimerValues.get(position);
        timerDigitView.setText(value);
    }

    private Runnable mUpdateDurationRunnable = new Runnable() {
        @Override
        public void run() {
            setDuration(mTimerManager.getSecondsRemaining());
            mHandler.postDelayed(this, TimeUnit.SECONDS.toMillis(1));
        }
    };

    private TimerManager.TimerFinishedListener mTimerFinishedListener =
        new TimerManager.TimerFinishedListener() {
            @Override
            public void onTimerFinished() {
                onTimerStopped();
            }
        };
}
