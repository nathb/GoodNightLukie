package com.nathb.goodnightlukie.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nathb.goodnightlukie.GoodNightApp;
import com.nathb.goodnightlukie.R;
import com.nathb.goodnightlukie.service.sound.SoundManager;
import com.nathb.goodnightlukie.service.timer.TimerManager;

import javax.inject.Inject;

public class MainActivity extends ActionBarActivity {

    @Inject SoundManager mSoundManager;
    @Inject TimerManager mTimerManager;

    private static final String[] TITLES = { "Sounds", "Timer" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoodNightApp.inject(this);
        setContentView(R.layout.activity_main);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new PageAdapter());
        mSoundManager.addListener(mPlaybackStateChangedListener);
        mTimerManager.addListener(mTimerFinishedListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundManager.removeListener(mPlaybackStateChangedListener);
        mTimerManager.removeListener(mTimerFinishedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem playButton = menu.findItem(R.id.Play);
        playButton.setVisible(!mSoundManager.isPlaying());
        final MenuItem stopButton = menu.findItem(R.id.Stop);
        stopButton.setVisible(mSoundManager.isPlaying());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Play:
                mSoundManager.play();
                invalidateOptionsMenu();
                return true;
            case R.id.Stop:
                mSoundManager.stop();
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class PageAdapter extends FragmentPagerAdapter {

        private static final int PAGE_COUNT = 2;

        public PageAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int i) {
            return i == 0
                    ? SoundFragment.newInstance()
                    : TimerFragment.newInstance();
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

    private SoundManager.PlaybackStateChangedListener mPlaybackStateChangedListener =
        new SoundManager.PlaybackStateChangedListener() {
            @Override
            public void onPlaybackStateChanged() {
                invalidateOptionsMenu();
            }
        };

    private TimerManager.TimerFinishedListener mTimerFinishedListener =
        new TimerManager.TimerFinishedListener() {
            @Override
            public void onTimerFinished() {
                invalidateOptionsMenu();
            }
        };

}
